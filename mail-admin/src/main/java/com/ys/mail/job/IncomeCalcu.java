package com.ys.mail.job;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ys.mail.entity.SmsFlashPromotion;
import com.ys.mail.entity.SmsFlashPromotionProduct;
import com.ys.mail.entity.UmsIncome;
import com.ys.mail.enums.SettingTypeEnum;
import com.ys.mail.mapper.*;
import com.ys.mail.model.dto.ESGroupMaster;
import com.ys.mail.model.dto.GroupMater;
import com.ys.mail.model.dto.TemporaryWorkers;
import com.ys.mail.service.SysSettingService;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.IdWorker;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author liky
 * @date 2021/12/29 15:12
 * @description 收益结算-暂存
 */
@Component
public class IncomeCalcu {

    @Autowired
    private SmsFlashPromotionProductMapper flashPromotionProductMapper;
    @Autowired
    private SmsFlashPromotionMapper flashPromotionMapper;
    @Autowired
    private UmsUserInviteMapper umsUserInviteMapper;
    @Autowired
    private UmsIncomeMapper umsIncomeMapper;
    @Autowired
    private PmsProductMapper productMapper;
    @Autowired
    private OmsOrderMapper omsOrderMapper;
    @Autowired
    private RestHighLevelClient client;
    @Autowired
    private SysSettingService settingService;

    private final static Logger LOGGER = LoggerFactory.getLogger(IncomeCalcu.class);

    /**
     * 每日团长结算
     */
    @Transactional(rollbackFor = Exception.class)
    public void parentIncome(List<Long> flashPromotionIds) throws IOException {
        // 规则：团长当日参与至少一单   以及 下级有人参与秒杀  才会有团长分佣收益
        LOGGER.info("========团长每日结算 start========");
        List<SmsFlashPromotion> smsFlashPromotions = flashPromotionMapper.selectBatchIds(flashPromotionIds);
        if (ObjectUtil.isEmpty(smsFlashPromotions)) {
            return;
        }
        SmsFlashPromotion smsFlashPromotion = smsFlashPromotions.get(0);
        Date endTime = smsFlashPromotion.getEndTime();
        String ymd = new SimpleDateFormat("yyyyMMdd").format(endTime);
        String ym = new SimpleDateFormat("yyyyMM").format(endTime);
        LOGGER.info("团长每日结算日期:{}", ymd);
        // 查询 今日有效团长ids
        // -> 去es 查询
        SearchRequest request = new SearchRequest("group_master");
        // TODO:待优化
        request.source().size(10000).query(QueryBuilders.termQuery("gameDate", ymd));

        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();
        List<SearchHit> searchHitList = Arrays.asList(hits.getHits());
        // 团长ids
        List<Long> hitsIds = searchHitList.stream().map(SearchHit::getId).map(Long::parseLong)
                                          .collect(Collectors.toList());
        if (BlankUtil.isEmpty(hitsIds)) {
            LOGGER.error("今日无团长消费");
        }
        List<TemporaryWorkers> twList = umsUserInviteMapper.findUserIdsByParentId();
        Map<Long, List<Long>> userMap = twList.stream()
                                              .collect(Collectors.toMap(TemporaryWorkers::getParentId, TemporaryWorkers::getUserIds));

        // 计算 团长今天收益
        for (Long pid : hitsIds) {
            List<Long> userids = userMap.get(pid);
            if (ObjectUtil.isEmpty(userids)) {
                LOGGER.error("无下级却是团长请检查es数据,团长id:{}", pid);
                continue;
            }
            // 今天0 = {Long@16225} 1470366909475196928下用户产生的 金额,这里出的问题,明天解决
            Long money = omsOrderMapper.queryMoney(userids, ymd);
            if (ObjectUtil.isEmpty(money) || money == 0L) {
                LOGGER.info("今日,团长下级无产生金额,团长id:{}", pid);
                continue;
            }
            LOGGER.info("-----今日,团长下级产生金额:{},团长id:{}", money, pid);
            BigDecimal moneyBg = BigDecimal.valueOf(money);
            // es 团长比例
            GetRequest getRequest = new GetRequest("group_master").id(pid.toString());
            ESGroupMaster parse = JSON.parseObject(client.get(getRequest, RequestOptions.DEFAULT)
                                                         .getSourceAsString(), ESGroupMaster.class);
            List<GroupMater> collect = parse.getEarnings().stream()
                                            .filter(obj -> obj.getDate().equals(Integer.valueOf(ym)))
                                            .collect(Collectors.toList());
            GroupMater groupMater = new GroupMater();
            Double ratio = settingService.getSettingValue(SettingTypeEnum.four);
            if (ObjectUtil.isEmpty(collect)) {
                ratio = BlankUtil.isEmpty(ratio) ? 0.002D : ratio;
                groupMater.setRatio(ObjectUtil.equal(ratio, NumberUtils.DOUBLE_ZERO) ? 0.002D : ratio);
            } else {
                groupMater = collect.get(0);
            }

            BigDecimal ratioBg = BigDecimal.valueOf(groupMater.getRatio());
            LOGGER.info("团长涨幅比例为:{}", ratioBg.doubleValue());
            // 团长收益
            BigDecimal divide = moneyBg.multiply(ratioBg);
            LOGGER.info("团长收益为:{}", divide.doubleValue());

            // {收益}---------并发
            UmsIncome newest = umsIncomeMapper.selectNewestByUserId(pid);
            // 收益表.(最新)今日收益
            Long todayIncome = newest != null ? newest.getTodayIncome() : 0L;
            // 收益表.(最新)总收益
            Long allIncome = newest != null ? newest.getAllIncome() : 0L;
            // 收益表.(最新)结余
            Long balance = newest != null ? newest.getBalance() : 0L;

            // Bg.(最新)今日收益
            BigDecimal todayIncomeBg = new BigDecimal(todayIncome);
            // Bg.(最新)总收益
            BigDecimal allIncomeBg = new BigDecimal(allIncome);
            // Bg.(最新)结余
            BigDecimal balanceBg = new BigDecimal(balance);

            // 此次计算今日收益
            BigDecimal todayIncomeBgAddIncome = todayIncomeBg.add(divide);
            // 此次总收益
            BigDecimal allIncomeBgAddIncome = allIncomeBg.add(divide);
            // 此次结余
            BigDecimal balanceBgBgAddIncome = balanceBg.add(divide);
            // 收益表数据
            UmsIncome umsIncome = new UmsIncome();
            // 收益ID
            umsIncome.setIncomeId(IdWorker.generateId());
            // 收益人
            umsIncome.setUserId(pid);
            // 收益数
            umsIncome.setIncome(divide.longValue());
            // 收益积分
            umsIncome.setIntegral(divide.longValue());
            // 今日收益
            umsIncome.setTodayIncome(todayIncomeBgAddIncome.longValue());
            // 总收益
            umsIncome.setAllIncome(allIncomeBgAddIncome.longValue());
            // 结余
            umsIncome.setBalance(balanceBgBgAddIncome.longValue());
            // 收益类型  1-秒杀收益  2022-02-11 修改为 6 -分佣(团长收益)
            umsIncome.setIncomeType(6);
            // 增加到余额收入
            umsIncome.setPayType(3);
            // 来源
            umsIncome.setDetailSource("平台每日分佣收入");
            // 备注
            umsIncome.setRemark("平台每日分佣收入-下级产生的金额总值:{" + money + "},团长id:{" + pid + "},涨幅比例:{" + ratioBg.doubleValue() + "}");
            umsIncomeMapper.insert(umsIncome);
        }
        LOGGER.info("========团长每日结算 end========");
    }

    /**
     * 场次结束 10分钟后 才触发平台回购
     */
    @Transactional(rollbackFor = Exception.class)
    public void userIncome(Long flashPromotionId) throws IOException {
        // 获取当前场次
        // --------计算所有当前场次用户的收益---------------------
        LOGGER.info("========用户未上架场次修改 start========{}", flashPromotionId);
        /*查询出用户未被秒杀的商品 flashPromotionPdtId List<Long> promotionPdtIds=flashPromotionProductMapper.selectByPromotionId(flashPromotionId);*/
        List<SmsFlashPromotionProduct> promotionProducts = flashPromotionProductMapper.
                selectList(new QueryWrapper<SmsFlashPromotionProduct>().
                        eq("flash_promotion_id", flashPromotionId).eq("flash_product_status", NumberUtils.INTEGER_TWO));
        if (ObjectUtil.isEmpty(promotionProducts)) {
            return;
        }
        /*flashPromotionProductMapper.updateFlashPromotionId(flashPromotionId);*/
        flashPromotionProductMapper.updateByPromotionId(promotionProducts);
        LOGGER.info("========修改用户上架数量 end========");
    }


}
