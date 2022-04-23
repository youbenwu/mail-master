package com.ys.mail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.entity.PcReview;
import com.ys.mail.entity.UmsIncome;
import com.ys.mail.entity.UmsUser;
import com.ys.mail.enums.EnumSettingType;
import com.ys.mail.exception.ApiException;
import com.ys.mail.mapper.PcReviewMapper;
import com.ys.mail.model.CommonResult;
import com.ys.mail.service.IncomeService;
import com.ys.mail.service.ReviewService;
import com.ys.mail.service.SysSettingService;
import com.ys.mail.service.UmsIncomeService;
import com.ys.mail.util.*;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Desc
 * @Author CRH
 * @Create 2021-12-30 15:46
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class ReviewServiceImpl extends ServiceImpl<PcReviewMapper, PcReview> implements ReviewService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewServiceImpl.class);

    @Autowired
    private PcReviewMapper pcReviewMapper;
    @Autowired
    private UmsIncomeService umsIncomeService;
    @Autowired
    private SysSettingService sysSettingService;
    @Autowired
    private IncomeService incomeService;

    @Override
    public List<PcReview> selectList(Long reviewId, Long pageSize) {
        Long userId = UserUtil.getCurrentUser().getUserId();
        return pcReviewMapper.getList(userId, reviewId, pageSize);
    }

    @Override
    public PcReview getNewestRecord(Long userId, String today) {
        QueryWrapper<PcReview> pcReviewQueryWrapper = new QueryWrapper<>();
        pcReviewQueryWrapper.eq("user_id", userId).eq("DATE(create_time)", today).orderByDesc("create_time")
                            .last("LIMIT 1");
        return this.getOne(pcReviewQueryWrapper);
    }

    @Override
    public CommonResult<Boolean> cancel(Long reviewId) {
        // 读取设置（是否允许取消、取消截止时间）
        String time = sysSettingService.getSettingValue(EnumSettingType.seventeen);// APP取消提现审核截止时间点,如 23:30
        if (DateTool.localTimeIsAfter(time)) return CommonResult.failed("已超过取消时间点，需要等系统自动退还", false);
        // 获取当前用户
        UmsUser currentUser = UserUtil.getCurrentUser();
        Long userId = currentUser.getUserId();
        String alipayName = currentUser.getAlipayName();
        LOGGER.info("用户【ID：{}，支付宝名称：{}】发起了取消审核", userId, alipayName);
        // 获取指定的审核记录
        PcReview pcReview = this.getById(reviewId);
        if (BlankUtil.isEmpty(pcReview)) return CommonResult.failed("审核ID异常！", false); // 校验审核id是否存在
        // 校验申请用户与当前操作用户身份是否匹配
        if (!pcReview.getUserId().equals(userId)) return CommonResult.failed("身份不匹配，禁止操作！", false);
        // 校验状态是否为0
        Integer reviewState = pcReview.getReviewState();
        if (!PcReview.ReviewState.ZERO.key().equals(reviewState)) return CommonResult.failed("审核状态异常！", false);
        // 获取最新流水记录
        UmsIncome income = umsIncomeService.selectNewestByUserId(userId);
        if (BlankUtil.isEmpty(income)) return CommonResult.failed("记录异常，无法取消", false);
        // 生成流水ID
        Long incomeId = IdWorker.generateId();
        // 修改审核记录
        pcReview.setReviewState(PcReview.ReviewState.FOUR.key());
        pcReview.setReviewDescribe("手动取消");
        pcReview.setIncomeId(String.valueOf(incomeId));
        int update = pcReviewMapper.updateById(pcReview);
        LOGGER.info("取消审核成功");
        if (update > NumberUtils.INTEGER_ZERO) {
            Long reviewMoney = pcReview.getReviewMoney();
            String actualMoney = DecimalUtil.longToStrForDivider(reviewMoney);
            // 退还资金
            UmsIncome umsIncome = UmsIncome.builder().incomeId(incomeId).userId(userId).income(reviewMoney)
                                           .expenditure(NumberUtils.LONG_ZERO).todayIncome(income.getTodayIncome())
                                           .allIncome(income.getAllIncome()).balance(income.getBalance() + reviewMoney)
                                           .incomeType(UmsIncome.IncomeType.FIVE.key()) // 5->审核退还
                                           .incomeNo("").orderTradeNo("").detailSource("用户手动取消提现:" + actualMoney + "元")
                                           .payType(UmsIncome.PayType.THREE.key())// 退还到余额中
                                           .build();
            boolean save = umsIncomeService.save(umsIncome);
            if (!save) throw new ApiException("添加流水异常");
            // 重新查询
            income = incomeService.getLatestEntry(userId);
            // 退还手续费
            boolean result = incomeService.refundCharges(income, pcReview.getRateIncomeId());
            LOGGER.info("退还服务费结果：{}", result);
            return CommonResult.success(true);
        }
        LOGGER.info("取消审核失败");
        return CommonResult.failed("操作失败", false);
    }
}
