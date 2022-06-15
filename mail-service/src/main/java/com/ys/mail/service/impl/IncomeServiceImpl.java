package com.ys.mail.service.impl;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.entity.UmsIncome;
import com.ys.mail.enums.SettingTypeEnum;
import com.ys.mail.exception.ApiAssert;
import com.ys.mail.exception.ApiException;
import com.ys.mail.exception.code.CommonResultCode;
import com.ys.mail.mapper.UmsIncomeMapper;
import com.ys.mail.model.admin.vo.FreezeReMoneyVO;
import com.ys.mail.model.po.OriginalIntegralPO;
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
        BigDecimal rate = this.calculateCharges(money);
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
                // 计算本金和积分
                long rateMoney = rate.longValue();
                OriginalIntegralPO po = this.calculateOriginalIntegral(userId, income, rateMoney);
                Long incomeId = IdWorker.generateId();
                // 添加手续费流水
                UmsIncome umsIncome = UmsIncome.builder()
                                               .incomeId(incomeId)
                                               .userId(userId)
                                               .income(NumberUtils.LONG_ZERO)
                                               .expenditure(rateMoney)
                                               .original(po.getOriginal())
                                               .integral(po.getIntegral())
                                               .balance(newBalance.longValue())
                                               .todayIncome(income.getTodayIncome())
                                               .allIncome(income.getAllIncome())
                                               // 10->提现费用
                                               .incomeType(UmsIncome.IncomeType.TEN.key())
                                               .incomeNo("").orderTradeNo("")
                                               .detailSource("扣除提现服务费:" + DecimalUtil.longToStrForDivider(rateMoney) + "元")
                                               // 从余额中扣除
                                               .payType(UmsIncome.PayType.THREE.key())
                                               .build();
                boolean updateResult = this.save(umsIncome);
                if (!updateResult) {
                    throw new ApiException("添加手续费失败");
                }
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
        if (BlankUtil.isEmpty(rateIncomeId)) {
            return false;
        }
        return this.refundChargesBatch(Collections.singletonList(income), Collections.singletonList(rateIncomeId));
    }

    @Override
    public boolean refundChargesBatch(List<UmsIncome> incomes, List<Long> rateIncomeIds) {
        if (BlankUtil.isEmpty(rateIncomeIds) || BlankUtil.isEmpty(incomes)) {
            return false;
        }
        // 获取一批服务费记录
        List<UmsIncome> rateIncomeList = this.listByIds(rateIncomeIds);
        // 构造新的记录，生成一批流水ID
        List<Long> incomeIds = IdWorker.generateIds(rateIncomeIds.size());
        AtomicInteger index = new AtomicInteger();
        List<UmsIncome> addList = new ArrayList<>();
        List<Boolean> ignored = rateIncomeList.stream().map(rateIncome -> incomes.stream().anyMatch(income -> {
            if (rateIncome.getUserId().equals(income.getUserId())) {
                Long rate = rateIncome.getExpenditure();
                UmsIncome build = UmsIncome.builder().
                                           incomeId(incomeIds.get(index.get())).userId(income.getUserId())
                                           .income(rate).expenditure(NumberUtils.LONG_ZERO)
                                           .original(rateIncome.getOriginal()).integral(rateIncome.getIntegral())
                                           .todayIncome(income.getTodayIncome()).allIncome(income.getAllIncome())
                                           .balance(income.getBalance() + rate)
                                           // 11->退还服务费
                                           .incomeType(UmsIncome.IncomeType.ELEVEN.key())
                                           .incomeNo("").orderTradeNo("")
                                           .detailSource("退还提现服务费:" + DecimalUtil.longToStrForDivider(rate) + "元")
                                           // 退还到余额中
                                           .payType(UmsIncome.PayType.THREE.key())
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
    public BigDecimal calculateCharges(Long money) {
        // 读取设置
        BigDecimal penny = new BigDecimal("10");
        JSON rules = sysSettingService.getSettingValue(SettingTypeEnum.eighteen);
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
            if (rate.compareTo(penny) < 0) {
                rate = penny;
            }
            return rate;
        }
        return null;
    }

    @Override
    public List<FreezeReMoneyVO> getByFreezeReMoney(String format) {
        return umsIncomeMapper.selectByFreezeReMoney(format);
    }

    /**
     * balance original integral amount
     * 1.1000 = (800)-1000  (500)-200
     * 800 - 1000 = -200 < 0 (表示不够扣除，并且只扣除了 800，还需要使用积分继续扣除)
     * 500 - abs(-200) = 300 (使用积分减去上一步的绝对值，则表示扣除完毕，且扣了200)
     * 2.500 = (800)-500 = 300 > 0 (表示足够扣除，并且已经扣除了500)
     *
     * @param umsIncome 最新收益
     * @param amount    需要扣除的总金额
     * @return 计算结果
     */
    @Override
    public OriginalIntegralPO calculateOriginalIntegral(Long userId, UmsIncome umsIncome, Long amount) {
        ApiAssert.isTrue(BlankUtil.isEmpty(umsIncome) || BlankUtil.isEmpty(amount), CommonResultCode.ERR_INTERFACE_PARAM);

        Long balance = umsIncome.getBalance();
        // 校验余额是否足够
        ApiAssert.isTrue(balance < amount, CommonResultCode.ERR_USER_DEPOSIT);
        // 获取剩余本金、积分
        OriginalIntegralPO po = umsIncomeMapper.getOriginalIntegralByUserId(userId);
        ApiAssert.noValue(po, CommonResultCode.ID_NO_EXIST);

        Long original = po.getOriginal();
        Long integral = po.getIntegral();
        // 这里只要为false，则首次使用余额进行计算（余额不在此处校验）
        if (!po.getCover()) {
            // 并且更新所有余额作为本金
            umsIncome.setOriginal(balance);
            this.updateById(umsIncome);
            original = balance;
        }
        // 优先扣除本金，再扣除积分
        long r1 = original - amount;
        if (r1 < 0) {
            r1 = Math.abs(r1);
            original = amount - r1;
            ApiAssert.isTrue(integral < r1, CommonResultCode.ERR_USER_DEPOSIT);
            integral = r1;
        } else {
            original = amount;
            integral = NumberUtils.LONG_ZERO;
        }
        return new OriginalIntegralPO(original, integral, true);
    }
}
