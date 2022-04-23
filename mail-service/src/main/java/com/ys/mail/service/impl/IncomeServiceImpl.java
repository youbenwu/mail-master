package com.ys.mail.service.impl;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.entity.UmsIncome;
import com.ys.mail.enums.EnumSettingType;
import com.ys.mail.exception.ApiException;
import com.ys.mail.mapper.UmsIncomeMapper;
import com.ys.mail.service.IncomeService;
import com.ys.mail.service.SysSettingService;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.DecimalUtil;
import com.ys.mail.util.IdWorker;
import com.ys.mail.util.NumberUtil;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @Desc 通用收益流水实现
 * @Author CRH
 * @Create 2022-03-21 11:15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class IncomeServiceImpl extends ServiceImpl<UmsIncomeMapper, UmsIncome> implements IncomeService {

    private final static Logger LOGGER = LoggerFactory.getLogger(IncomeServiceImpl.class);

    @Autowired
    private SysSettingService sysSettingService;
    @Autowired
    private UmsIncomeMapper umsIncomeMapper;

    @Override
    public Map<String, Long> deductCharges(UmsIncome income, Long money, Long userId) {
        // 计算服务费
        BigDecimal rate = this.calcuCharges(money);
        Map<String, Long> resultMap = new HashMap<>(3);
        if (BlankUtil.isNotEmpty(rate)) {
            BigDecimal zeroOne = new BigDecimal("10");
            BigDecimal newMoney = new BigDecimal(money);
            // 尝试扣除服务费
            BigDecimal balance = new BigDecimal(income.getBalance());
            BigDecimal newBalance = balance.subtract(rate);
            // 检查扣除完服务费的余额是否小于0.1，如果小于0.1则不需要扣除手续费
            if (newBalance.compareTo(zeroOne) >= 0) {
                // 检查余额是否足够提现
                if (newBalance.compareTo(newMoney) < 0) {
                    newMoney = newBalance;
                }
                Long incomeId = IdWorker.generateId();
                // 添加手续费流水
                UmsIncome umsIncome = UmsIncome.builder()
                                               .incomeId(incomeId)
                                               .userId(userId)
                                               .income(NumberUtils.LONG_ZERO)
                                               .expenditure(rate.longValue())
                                               .balance(newBalance.longValue())
                                               .todayIncome(income.getTodayIncome())
                                               .allIncome(income.getAllIncome())
                                               .incomeType(UmsIncome.IncomeType.TEN.key()) // 10->提现费用
                                               .incomeNo("").orderTradeNo("")
                                               .detailSource("扣除提现服务费:" + DecimalUtil.longToStrForDivider(rate.longValue()) + "元")
                                               .payType(UmsIncome.PayType.THREE.key()) // 从余额中扣除
                                               .build();
                boolean updateResult = this.save(umsIncome);
                if (!updateResult) throw new ApiException("添加手续费失败");
                resultMap.put("rateIncomeId", incomeId);
            } else {
                newBalance = balance;
            }
            // 返回最新余额和提现金额
            resultMap.put("balance", newBalance.longValue());
            resultMap.put("money", newMoney.longValue());
        }
        return resultMap;
    }

    @Override
    public boolean refundCharges(UmsIncome income, Long rateIncomeId) {
        // 判断是否存在手续费，不存在不需要退还
        if (BlankUtil.isEmpty(rateIncomeId)) return false;
        return this.refundChargesBatch(Collections.singletonList(income), Collections.singletonList(rateIncomeId));
    }

    @Override
    public boolean refundChargesBatch(List<UmsIncome> incomes, List<Long> rateIncomeIds) {
        if (BlankUtil.isEmpty(rateIncomeIds) || BlankUtil.isEmpty(incomes)) return false;
        // 获取一批服务费记录
        List<UmsIncome> rateIncomeList = this.listByIds(rateIncomeIds);
        // 构造新的记录
        List<Long> incomeIds = IdWorker.generateIds(rateIncomeIds.size()); // 生成一批流水ID
        AtomicInteger index = new AtomicInteger();
        List<UmsIncome> addList = new ArrayList<>();
        List<Boolean> collect = rateIncomeList.stream().map(map -> incomes.stream().anyMatch(income -> {
            if (map.getUserId().equals(income.getUserId())) {
                Long rate = map.getExpenditure();
                UmsIncome build = UmsIncome.builder().
                                           incomeId(incomeIds.get(index.get())).userId(income.getUserId())
                                           .income(rate).expenditure(NumberUtils.LONG_ZERO)
                                           .todayIncome(income.getTodayIncome()).allIncome(income.getAllIncome())
                                           .balance(income.getBalance() + rate)
                                           .incomeType(UmsIncome.IncomeType.ELEVEN.key()) // 11->退还服务费
                                           .incomeNo("").orderTradeNo("")
                                           .detailSource("退还提现服务费:" + DecimalUtil.longToStrForDivider(rate) + "元")
                                           .payType(UmsIncome.PayType.THREE.key())// 退还到余额中
                                           .build();
                addList.add(build);
                index.getAndIncrement();
                return true;
            }
            return false;
        })).collect(Collectors.toList());
        // 添加到数据库
        return this.saveBatch(addList);
    }

    @Override
    public UmsIncome getLatestEntry(Long userId) {
        return umsIncomeMapper.selectNewestByUserId(userId);
    }

    @Override
    public BigDecimal calcuCharges(Long money) {
        // 读取设置
        BigDecimal penny = new BigDecimal("10");
        JSON rules = sysSettingService.getSettingValue(EnumSettingType.eighteen);
        if (BlankUtil.isNotEmpty(rules)) {
            // 解析规则
            JSONArray parseArray = JSONUtil.parseArray(rules);
            // 除100
            Double dMoney = DecimalUtil.longToDoubleForDivider(money);
            // 获取费率
            String scale = NumberUtil.getScale(parseArray, dMoney, "0.001");
            // 计算服务费(使用上舍模式)
            BigDecimal newMoney = new BigDecimal(money);
            BigDecimal rate = newMoney.multiply(new BigDecimal(scale))
                                      .setScale(0, RoundingMode.UP);
            // 保证最小服务费为0.1
            if (rate.compareTo(penny) < 0) rate = penny;
            return rate;
        }
        return null;
    }
}
