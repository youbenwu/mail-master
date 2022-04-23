package com.ys.mail.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.response.AlipayFundTransUniTransferResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.constant.AlipayConstant;
import com.ys.mail.constant.StringConstant;
import com.ys.mail.entity.PcReview;
import com.ys.mail.entity.PcUser;
import com.ys.mail.entity.UmsIncome;
import com.ys.mail.enums.EnumSettingType;
import com.ys.mail.exception.ApiException;
import com.ys.mail.mapper.PcReviewMapper;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.param.PcReviewParam;
import com.ys.mail.model.admin.query.PcReviewQuery;
import com.ys.mail.model.admin.vo.PcReviewVO;
import com.ys.mail.model.alipay.AlipayPaidOutParam;
import com.ys.mail.model.alipay.BusinessParams;
import com.ys.mail.model.alipay.PayeeInfo;
import com.ys.mail.override.ChainLinkedHashMap;
import com.ys.mail.service.*;
import com.ys.mail.util.*;
import com.ys.mail.wrapper.SqlQueryWrapper;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 070
 * @since 2021-12-29
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PcReviewServiceImpl extends ServiceImpl<PcReviewMapper, PcReview> implements PcReviewService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PcReviewServiceImpl.class);
    @Autowired
    private PcReviewMapper pcReviewMapper;
    @Autowired
    private UmsIncomeService umsIncomeService;
    @Autowired
    private CommonPayService commonPayService;
    @Autowired
    private SysSettingService sysSettingService;
    @Autowired
    private IncomeService incomeService;

    @Override
    public CommonResult<IPage<PcReviewVO>> getPage(PcReviewQuery query) {
        IPage<PcReviewVO> page = new Page<>(query.getPageNum(), query.getPageSize());
        SqlQueryWrapper<PcReviewVO> wrapper = new SqlQueryWrapper<>();
        wrapper.eq("pr.review_state", query.getKeyword()).like("pr.alipay_account", query.getAlipayAccount())
               .like("pr.alipay_name", query.getAlipayName()).like("pr.income_id", query.getIncomeId())
               .eq("pr.user_id", query.getUserId())
               .compareDate("pr.create_time", query.getBeginTime(), query.getEndTime())
               .orderByAsc(StringConstant.STRING_ZERO.equals(query.getKeyword()), "pr.review_id")
               .orderByDesc(!StringConstant.STRING_ZERO.equals(query.getKeyword()), "pr.review_id");
        return CommonResult.success(pcReviewMapper.getPage(page, wrapper));
    }

    @Override
    public CommonResult<Boolean> updateReview(PcReviewParam param) {
        // 是否开启后台审核功能
        Boolean openReview = sysSettingService.getSettingValue(EnumSettingType.twelve);
        // 是否开启后台审核转账
        Boolean openPaidOut = sysSettingService.getSettingValue(EnumSettingType.thirteen);
        // 暂停后台审核
        if (!openReview) {
            return CommonResult.failed("系统已经暂停后台审核，请联系管理员", Boolean.FALSE);
        }

        // 获取指定的审核记录
        PcReview pcReview = this.getById(param.getReviewId());
        if (BlankUtil.isEmpty(pcReview)) {
            return CommonResult.failed("审核ID异常！");
        }
        String reviewState = param.getReviewState();
        Integer dbReviewState = pcReview.getReviewState();
        // 原始金额
        Long reviewMoney = pcReview.getReviewMoney();
        // 实际金额
        String actualMoney = DecimalUtil.longToStrForDivider(reviewMoney);
        // 当前需要审核的用户
        Long reviewUserId = pcReview.getUserId();
        // 后台系统登录用户
        PcUser pcUser = PcUserUtil.getCurrentUser();
        Long pcUserId = pcUser.getPcUserId();
        String pcUserUsername = pcUser.getUsername();
        LOGGER.info("【审核日志】[审核人ID：{}，审核人名称：{}] ==> [ID：{}，名称：{}，账号:{}，提现金额：{}]的用户进行了提现审核", pcUserId, pcUserUsername, reviewUserId, pcReview.getAlipayName(), pcReview.getAlipayAccount(), actualMoney);

        // 查询最新的账户收益，不能使用时间判断
        List<UmsIncome> latestIncome = umsIncomeService.selectLatestByUserIds(Collections.singletonList(reviewUserId));
        if (BlankUtil.isEmpty(latestIncome)) {
            LOGGER.info("没有收益无法提现审核");
            throw new ApiException("没有收益无法提现审核");
        }
        UmsIncome umsIncome = latestIncome.get(0);

        Long incomeId;
        boolean ifCondition = NumberUtils.INTEGER_ZERO.equals(dbReviewState) && (StringConstant.STRING_TWO.equals(reviewState) || StringConstant.STRING_THREE.equals(reviewState));
        // 审核通过后系统将直接转账
        if (Objects.equals(dbReviewState, NumberUtils.INTEGER_ZERO) && StringConstant.STRING_ONE.equals(reviewState)) {
            if (openPaidOut) {
                CommonResult<Boolean> commonResult = this.transferAccounts(pcReview.getAlipayName(), pcReview.getAlipayAccount(), actualMoney);
                Boolean result = commonResult.getData();
                if (BlankUtil.isNotEmpty(result)) {
                    incomeId = this.addPaidOutIncome(reviewUserId, reviewMoney, commonResult.getMessage(), umsIncome, true, "系统转账");
                } else {
                    return commonResult;
                }
            } else {
                incomeId = this.addPaidOutIncome(reviewUserId, reviewMoney, "", umsIncome, true, "线下转账");
            }
        } else if (ifCondition) {
            // 将冻结金额原路返回到余额中
            incomeId = this.addPaidOutIncome(reviewUserId, reviewMoney, "", umsIncome, false, param.getReviewDescribe());
            // 重新查询
            umsIncome = incomeService.getLatestEntry(reviewUserId);
            // 退还提现服务费
            boolean result = incomeService.refundCharges(umsIncome, pcReview.getRateIncomeId());
            LOGGER.info("退还服务费结果：{}", result);
        } else {
            throw new ApiException("更新失败，请检查");
        }

        // 更新审核状态
        return this.updateState(pcReview, param, pcUserId, incomeId);
    }

    /**
     * 系统转账
     *
     * @param alipayName    支付宝名称
     * @param alipayAccount 支付宝账号
     * @param actualMoney   转账金额
     * @return 是否转账成功
     */
    private CommonResult<Boolean> transferAccounts(String alipayName, String alipayAccount, String actualMoney) {
        String orderSn = IdGenerator.INSTANCE.generateId();
        // 构建转账对象
        AlipayPaidOutParam build = AlipayPaidOutParam.builder().outBizNo(orderSn).transAmount(actualMoney)
                                                     .productCode(AlipayConstant.PRODUCT_CODE)
                                                     .bizScene(AlipayConstant.BIZ_SCENE)
                                                     .orderTitle(AlipayConstant.USER_DEPOSIT)
                                                     .payeeInfo(PayeeInfo.builder()
                                                                         .identityType(AlipayConstant.IDENTITY_TYPE_LOGON)
                                                                         .name(alipayName).identity(alipayAccount)
                                                                         .build())
                                                     .remark(orderSn + alipayName + AlipayConstant.USER_DEPOSIT)
                                                     .businessParams(BusinessParams.builder()
                                                                                   .payerShowName(AlipayConstant.JH_KJ + AlipayConstant.USER_DEPOSIT)
                                                                                   .build()).build();
        boolean responseSuccess;
        AlipayFundTransUniTransferResponse response;
        try {
            response = commonPayService.paidOut(build);
            JSONObject result = JSONObject.parseObject(response.getBody());
            responseSuccess = response.isSuccess();
            LOGGER.info("支付宝提现结果{}", result);
        } catch (AlipayApiException e) {
            e.printStackTrace();
            throw new ApiException("支付宝提现异常");
        }
        // 转账异常返回结果
        if (!responseSuccess && BlankUtil.isNotEmpty(response) && BlankUtil.isNotEmpty(response.getSubMsg())) {
            LOGGER.error("【审核日志】-转账异常：{}", response.getSubMsg());
            return CommonResult.failed(response.getSubMsg());
        }
        LOGGER.info("【审核日志】系统成功转账给用户[支付宝名称:{}，支付宝账号:{}]，金额[{}]元", alipayName, alipayAccount, actualMoney);
        return CommonResult.success(response.getOrderId(), Boolean.TRUE);
    }

    /**
     * 添加转账流水
     *
     * @param userId        转账对象：用户ID
     * @param reviewMoney   审核金额
     * @param aliPayOrderId 支付订单流水号
     * @param latestIncome  最新的收益
     * @param opsType       流水类型：true->提现，false->审核退还
     * @return 系统流水号
     */
    private Long addPaidOutIncome(Long userId, Long reviewMoney, String aliPayOrderId, UmsIncome latestIncome, Boolean opsType, String remark) {
        Long incomeId = IdWorker.generateId();
        String orderSn = IdGenerator.INSTANCE.generateId();
        // 实际金额
        String actualMoney = DecimalUtil.longToStrForDivider(reviewMoney);

        UmsIncome umsIncome;
        UmsIncome.UmsIncomeBuilder builder = UmsIncome.builder().incomeId(incomeId)
                                                      // 转给哪一个用户
                                                      .userId(userId).expenditure(NumberUtils.LONG_ZERO)
                                                      // 总流水不变
                                                      .allIncome(latestIncome.getAllIncome())
                                                      // 今日流水不变
                                                      .todayIncome(latestIncome.getTodayIncome()).remark(remark);

        if (opsType) {
            umsIncome = builder.income(NumberUtils.LONG_ZERO)
                               // 由于余额已被系统提前冻结，所以直接保存即可
                               .balance(latestIncome.getBalance())
                               // 2->余额提现
                               .incomeType(NumberUtils.INTEGER_TWO).incomeNo(aliPayOrderId).orderTradeNo(orderSn)
                               .detailSource("提现到账成功:" + actualMoney + "元").payType(NumberUtils.INTEGER_TWO).build();
        } else {
            umsIncome = builder.income(reviewMoney).balance(latestIncome.getBalance() + reviewMoney)
                               // 5->审核退还
                               .incomeType(UmsIncome.IncomeType.FIVE.key()).incomeNo("").orderTradeNo("")
                               .detailSource("系统退还审核金额:" + actualMoney + "元")
                               // 退还到余额中
                               .payType(UmsIncome.PayType.THREE.key()).build();
            LOGGER.info("【审核日志】系统退还给[用户:" + userId + "]的提现金额为" + reviewMoney + "元！");
        }
        boolean updateResult = umsIncomeService.save(umsIncome);
        if (!updateResult) {
            throw new ApiException(String.format("【审核日志】添加流水异常,用户[id:%d]", userId));
        }
        return incomeId;
    }

    /**
     * 更新审核状态
     *
     * @param pcReview 当前审核记录
     * @param param    修改参数
     * @param pcUserId 登录用户ID
     * @param incomeId 流水号
     * @return 是否更新成功
     */
    private CommonResult<Boolean> updateState(PcReview pcReview, PcReviewParam param, Long pcUserId, Long incomeId) {
        Integer dbReviewState = pcReview.getReviewState();
        // 更新审核
        if (Objects.equals(dbReviewState, NumberUtils.INTEGER_ZERO)) {
            LOGGER.info("更新后状态为：{}", param.getReviewState());
            // 审核状态
            pcReview.setReviewState(Integer.valueOf(param.getReviewState()));
            // 审核描述
            pcReview.setReviewDescribe(param.getReviewDescribe());
            // 审核人
            pcReview.setPcUserId(pcUserId);
            pcReview.setIncomeId(BlankUtil.isNotEmpty(incomeId) ? String.valueOf(incomeId) : null);
            // 更新审核记录
            boolean updateResult = this.saveOrUpdate(pcReview);
            if (!updateResult) {
                throw new ApiException("审核修改失败");
            }
            // TODO:推送消息通知到app端

            return CommonResult.success("提现审核成功", Boolean.TRUE);
        }
        return CommonResult.failed("提现审核失败");
    }

    @Override
    public List<PcReview> getTodayList(String time) {
        return pcReviewMapper.getTodayList(time);
    }

    @Override
    public void exportExcel(PcReviewQuery query, String fileName, HttpServletResponse response) {
        // 取消分页
        query.setPageSize(NumberUtils.INTEGER_MINUS_ONE);
        // 获取数据
        CommonResult<IPage<PcReviewVO>> result = this.getPage(query);
        // 获取记录
        List<PcReviewVO> records = result.getData().getRecords();
        // 工作表集合
        Map<String, List<Map<String, Object>>> workbookMap = new HashMap<>(1);
        // 封装数据
        List<Map<String, Object>> rows = new ArrayList<>();
        records.forEach(e -> {
            // 将records结果使用Map进行包装，方便自定义显示内容
            ChainLinkedHashMap<String, Object> map = new ChainLinkedHashMap<>();
            map.putObj("申请人ID", e.getUserId().toString())
               .putObj("支付宝名字", e.getAlipayName())
               .putObj("支付宝账号", e.getAlipayAccount())
               .putObj("申请提现金额", DecimalUtil.longToStrForDivider(e.getReviewMoney()))
               .putObj("审核状态", EnumTool.getValue(PcReview.ReviewState.class, e.getReviewState()))
               .putObj("审核人ID", BlankUtil.isNotEmpty(e.getReviewId()) ? e.getReviewId().toString() : null)
               .putObj("审核人名称", e.getUsername())
               .putObj("提现流水号", BlankUtil.isNotEmpty(e.getIncomeId()) ? e.getIncomeId() : null)
               .putObj("申请时间", e.getCreateTime())
               .putObj("审核时间", e.getUpdateTime())
               .putObj("审核描述", e.getReviewDescribe());
            rows.add(map);
        });
        workbookMap.put(fileName, rows);
        // 导出Excel
        ExcelTool.writeExcel(workbookMap, fileName, response);
    }

}
