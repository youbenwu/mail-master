package com.ys.mail.schedule;

import com.ys.mail.constant.FigureConstant;
import com.ys.mail.entity.PcReview;
import com.ys.mail.entity.PmsPartnerRe;
import com.ys.mail.entity.UmsIncome;
import com.ys.mail.model.admin.vo.PrPdtOrderVO;
import com.ys.mail.model.unionPay.DateUtil;
import com.ys.mail.model.vo.PartnerReIncomeVO;
import com.ys.mail.service.*;
import com.ys.mail.util.*;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Desc SpringBoot 异步定时调度类
 * @Author CRH
 * @Create 2022-01-07 19:09
 */
@Async
@Component
public class ScheduleTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleTask.class);

    @Autowired
    private PcReviewService pcReviewService;
    @Autowired
    private UmsIncomeService umsIncomeService;
    @Autowired
    private OmsOrderService omsOrderService;
    @Autowired
    private PmsPartnerReService partnerReService;
    @Autowired
    private IncomeService incomeService;

    /**
     * 每天晚上23:50:00 时间点：将当天所有未进行审核的数据置为[已失效]数据
     */
    @Scheduled(cron = "00 50 23 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void scheduledTask1() throws Exception {
        String taskName = "ScheduleTask1";
        try (CostTimeUtil t = new CostTimeUtil(taskName)) {
            String todayNow = DateTool.getTodayNow();
            // 获取当天所有待审核的审核记录，需要退还资金（查询）
            List<PcReview> todayReviewList = pcReviewService.getTodayList(todayNow);

            if (BlankUtil.isNotEmpty(todayReviewList)) {
                int size = todayReviewList.size();
                LOGGER.info("【{}】-[当天提现审核未处理的记录条数为：{}]", taskName, size);

                // 将冻结金额原路返回到余额中（插入）
                List<UmsIncome> addIncomeList = new ArrayList<>();
                List<Long> incomeIds = IdWorker.generateIds(todayReviewList.size()); // 生成一批流水ID
                // 从审核记录中获取待审核的用户ID集合
                List<Long> userIds = new ArrayList<>();
                todayReviewList.forEach(item -> userIds.add(item.getUserId()));
                // 从数据库中获取所有用户的最新记录
                List<UmsIncome> newIncomeList = umsIncomeService.selectLatestByUserIds(userIds);

                Date date = new Date();
                // 分别构建出新的Income对象，批量插入到流水表中
                newIncomeList.forEach(oldIncome -> {
                    PcReview review;
                    UmsIncome umsIncome;
                    for (int i = 0; i < todayReviewList.size(); i++) {
                        review = todayReviewList.get(i);
                        // 两者需要相等
                        if (oldIncome.getUserId().equals(review.getUserId())) {
                            Long incomeId = incomeIds.get(i);
                            umsIncome = UmsIncome.builder().incomeId(incomeId).userId(oldIncome.getUserId())
                                                 .income(review.getReviewMoney()).expenditure(NumberUtils.LONG_ZERO)
                                                 .balance(oldIncome.getBalance() + review.getReviewMoney())
                                                 .todayIncome(oldIncome.getTodayIncome())
                                                 .allIncome(oldIncome.getAllIncome()) // 不计算入总流水
                                                 .incomeType(UmsIncome.IncomeType.FIVE.key()) // 5->审核退还
                                                 .incomeNo("").orderTradeNo("")
                                                 .detailSource("系统退还审核金额:" + DecimalUtil.longToStrForDivider(review.getReviewMoney()) + "元")
                                                 .payType(UmsIncome.PayType.THREE.key())
                                                 .remark("系统定时调度触发").build();
                            addIncomeList.add(umsIncome);
                            review.setReviewState(PcReview.ReviewState.MINUS_ONE.key());
                            review.setPcUserId(null);
                            review.setReviewDescribe("由于已过审核时间，请明天重新申请");
                            review.setIncomeId(String.valueOf(incomeId));
                            review.setUpdateTime(date);
                        }
                    }
                });

                // 更新当天所有待审核的记录
                pcReviewService.updateBatchById(todayReviewList);
                LOGGER.info("【{}】-[系统后台审核数据置为失效]-批量更新成功，操作条数：{}", taskName, size);

                // 系统退还审核资金
                umsIncomeService.saveBatch(addIncomeList);
                LOGGER.info("【{}】-[系统后台审核退还用户提现]-批量添加成功，操作条数：{}", taskName, addIncomeList.size());

                // 退还提现手续费
                newIncomeList = umsIncomeService.selectLatestByUserIds(userIds);
                List<Long> rateIncomeIds = new ArrayList<>();
                todayReviewList.stream().map(PcReview::getRateIncomeId).forEach(rateIncomeId -> {
                    if (BlankUtil.isNotEmpty(rateIncomeId)) rateIncomeIds.add(rateIncomeId);
                });
                boolean result = incomeService.refundChargesBatch(newIncomeList, rateIncomeIds);
                LOGGER.info("【{}-[系统后台审核退还用户提现服务费]-结果：{}】", taskName, result);
            } else {
                LOGGER.info("【{}】-[由于记录条数为0，所以跳过执行]", taskName);
            }
        }
    }

    /**
     * 定时任务-每天凌晨1.00分执行当前线程,异步执行,不占用主线程
     * 查询前一天总共产生了多少笔合伙人订单,期数可以定义,6期-12期都可以定义,返还规则,
     * 这样定义,比如0->未返还,1->返还成功,2->返还失败,默认值为0,
     * 查询出前一天总共有多少人付款了合伙人订单,使用到映射,
     * 使用的Byte期数,整数不能超过-128到127,请注意!
     */
    @Transactional(rollbackFor = Exception.class)
    @Scheduled(cron = "00 00 1 * * ?")
    public void scheduledTask2() {
        LOGGER.info("异步执行产生的订单生成分佣开始---任务执行时间:{},线程名称:{}", LocalDateTime.now(), Thread.currentThread().getName());

        List<PrPdtOrderVO> vos = omsOrderService.getByPrPdtOrder();
        if (BlankUtil.isEmpty(vos)) {
            LOGGER.info("【scheduledTask2】没有调度数据");
            return;
        }
        List<PmsPartnerRe> partnerRes = new ArrayList<>();
        AtomicInteger num = new AtomicInteger();
        Integer reduce = vos.stream()
                            .reduce(NumberUtils.INTEGER_ZERO, (sum, p) -> sum += p.getRePeriods(), Integer::sum);
        List<Long> ids = IdWorker.generateIds(reduce);
        vos.stream().filter(Objects::nonNull).forEach(vo -> {
            // 相等才会进来
            if (BlankUtil.isNotEmpty(vo) && vo.getTotalAmount().equals(vo.getTotalPrice() * vo.getProductQuantity())) {
                Integer rePeriods = vo.getRePeriods();
                long partnerPrice = vo.getPartnerPrice() * vo.getProductQuantity();
                BigDecimal periods = DecimalUtil.toBigDecimal(partnerPrice)
                                                .divide(new BigDecimal(rePeriods), BigDecimal.ROUND_CEILING, RoundingMode.DOWN);
                BigDecimal divide = periods.divide(new BigDecimal(FigureConstant.INT_ONE_HUNDRED), BigDecimal.ROUND_CEILING, RoundingMode.DOWN);
                List<Date> dates = DateUtil.nextMonthFirstDates(rePeriods);
                for (int i = NumberUtils.BYTE_ONE; i <= rePeriods; i++) {
                    num.incrementAndGet();
                    int i1 = i - NumberUtils.INTEGER_ONE;
                    partnerRes.add(PmsPartnerRe.builder().partnerReId(ids.get(num.get() - NumberUtils.INTEGER_ONE))
                                               .userId(vo.getUserId()).orderId(vo.getOrderId()).periodsNum(i)
                                               .periodsPrice(periods.longValue())
                                               .descSour("用户:" + vo.getUserId() + "返还订单号:" + vo.getOrderId() + "金额:" + divide)
                                               .periodsDate(dates.get(i1)).build());
                }
            }
        });
        try {
            boolean b = partnerReService.saveBatch(partnerRes);
            LOGGER.info("执行:{},创建数量{}", b, partnerRes.size());
        } catch (Exception e) {
            LOGGER.info("scheduledTask2系统调度信息异常,当天执行的数量为0");
            e.printStackTrace();
        }
    }

    /**
     * 每月1号返还给用户的收益数据凌晨1.10分执行
     * 先查出来单月需要返还的金额有多少,同时收益表中查询出最新的收益进行返还,比如进行返还的是无数个用户,有的是秒了多单,
     * 肯定不能循环里面写sql,查询单月匹配有多少需要返佣的,单月1号的
     */
    @Transactional(rollbackFor = Exception.class)
    @Scheduled(cron = "00 10 1 1 * ?")
    public void scheduledTask3() {

        LOGGER.info("每月返还分佣开始---任务执行时间:{},线程名称:{}", LocalDateTime.now(), Thread.currentThread().getName());
        String format = new SimpleDateFormat(DateUtil.DT_SHORT_).format(new Date());
        List<PartnerReIncomeVO> vos = partnerReService.getByMonthFirstDates(format);
        if (BlankUtil.isEmpty(vos)) {
            LOGGER.info("scheduledTask3单月没有返还的数据");
            return;
        }
        List<Long> ids = IdWorker.generateIds(vos.size());
        List<UmsIncome> incomes = new ArrayList<>();
        Integer integerZero = NumberUtils.INTEGER_ZERO;
        AtomicInteger num = new AtomicInteger();
        vos.stream().filter(Objects::nonNull).forEach(vo -> {
            // income可能为空和不为空,为空和不为空都是新增,
            if (BlankUtil.isNotEmpty(vo) && vo.getReNum() > integerZero) {
                num.incrementAndGet();
                long periodsPrice = vo.getPeriodsPrice();
                long todayIncome = periodsPrice;
                long balance = periodsPrice;
                long allIncome = periodsPrice;
                if (BlankUtil.isNotEmpty(vo.getIncome()) && vo.getUserId().equals(vo.getIncome().getUserId())) {
                    UmsIncome income = vo.getIncome();
                    todayIncome = DateUtil.getDateFormat(income.getCreateTime(), DateUtil.DT_SHORT_)
                                          .equals(format) ? (BlankUtil.isEmpty(income.getTodayIncome()) ? NumberUtils.LONG_ZERO + periodsPrice : income.getTodayIncome() + periodsPrice) : periodsPrice;
                    balance = income.getBalance() + periodsPrice;
                    allIncome = income.getAllIncome() + periodsPrice;
                }
                BigDecimal price = DecimalUtil.toBigDecimal(periodsPrice)
                                              .divide(new BigDecimal(FigureConstant.INT_ONE_HUNDRED), BigDecimal.ROUND_CEILING, RoundingMode.DOWN);
                incomes.add(UmsIncome.builder().incomeId(ids.get(num.get() - NumberUtils.INTEGER_ONE))
                                     .userId(vo.getUserId()).income(vo.getPeriodsPrice())
                                     .expenditure(NumberUtils.LONG_ZERO).balance(balance).allIncome(allIncome)
                                     .todayIncome(todayIncome).incomeType(UmsIncome.IncomeType.EIGHT.key())
                                     .detailSource("用户:" + vo.getUserId() + "返还合伙人商品金额:" + price + "总计数量:" + vo.getReNum())
                                     .payType(UmsIncome.PayType.THREE.key()).incomeNo(FigureConstant.STRING_EMPTY)
                                     .orderTradeNo(FigureConstant.STRING_EMPTY).build());
            }
        });
        try {
            // 此处需注意:修改返还数量=统计需返还总数一致,前面已经判断过空所以不需要再判断了
            Optional<Integer> reduce = vos.stream().map(PartnerReIncomeVO::getReNum).reduce(Integer::sum);
            int save = umsIncomeService.insertBatch(incomes);
            int update = save > integerZero ? partnerReService.updateBatch(format) : integerZero;
            LOGGER.info("创建用户合伙人商品返佣数量:{},修改返还数量{},统计需返还总数:{}", save, update, reduce);
        } catch (Exception e) {
            LOGGER.info("scheduledTask3系统调度信息异常,当月1号执行的数量为0");
            e.printStackTrace();
        }
    }

}
