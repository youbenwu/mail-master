package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.UmsIncome;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Desc 通用收益流水接口
 * @Author CRH
 * @Create 2022-03-21 11:14
 */
public interface IncomeService extends IService<UmsIncome> {

    /**
     * 扣除提现手续费
     *
     * @param income 用户最新收益
     * @param money  提现金额
     * @param userId 当前用户ID
     * @return 最终余额、提现金额、手续费流水ID号
     */
    Map<String, Long> deductCharges(UmsIncome income, Long money, Long userId);

    /**
     * 退还提现服务费：针对需要审核的记录
     *
     * @param income       用户最新收益
     * @param rateIncomeId 之前扣除的记录ID
     * @return 结果
     */
    boolean refundCharges(UmsIncome income, Long rateIncomeId);

    /**
     * 批量退还服务费
     *
     * @param incomes       一批用户的最新收益
     * @param rateIncomeIds 一批服务费流水号
     * @return 结果
     */
    boolean refundChargesBatch(List<UmsIncome> incomes, List<Long> rateIncomeIds);

    /**
     * 根据用户ID获取最新一条收益流水
     *
     * @param userId 用户ID
     * @return 最新记录
     */
    UmsIncome getLatestEntry(Long userId);

    /**
     * 统一计算服务费
     *
     * @param money 需要计算的金额，乘以100的值
     * @return 服务费
     */
    BigDecimal calcuCharges(Long money);

}
