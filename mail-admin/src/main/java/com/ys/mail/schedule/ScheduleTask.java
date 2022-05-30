package com.ys.mail.schedule;

import com.ys.mail.constant.FigureConstant;
import com.ys.mail.entity.*;
import com.ys.mail.enums.SettingTypeEnum;
import com.ys.mail.model.admin.vo.FreezeReMoneyVO;
import com.ys.mail.model.admin.vo.OrdinaryReMoneyVO;
import com.ys.mail.model.admin.vo.PidPrPdtOrderVO;
import com.ys.mail.model.admin.vo.PrPdtOrderVO;
import com.ys.mail.model.unionPay.DateUtil;
import com.ys.mail.model.vo.PartnerReIncomeVO;
import com.ys.mail.service.*;
import com.ys.mail.util.*;
import com.ys.mail.wrapper.SqlLambdaQueryWrapper;
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
 * SpringBoot 异步定时调度类
 *
 * @author CRH
 * @date 2022-05-23 16:57
 * @since 1.0
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
    @Autowired
    private SysSettingService settingService;
    @Autowired
    private AmsAppService amsAppService;
    @Autowired
    private CosService cosService;

    /**
     * 每天晚上23:50:00 时间点：将当天所有未进行审核的数据置为[已失效]数据
     */
    @Scheduled(cron = "00 50 23 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void scheduledTask1() throws Exception {
        String taskName = "ScheduleTask1";
        try (CostTimeUtil ignored = new CostTimeUtil(taskName)) {
            String todayNow = DateTool.getTodayNow();
            // 获取当天所有待审核的审核记录，需要退还资金（查询）
            List<PcReview> todayReviewList = pcReviewService.getTodayList(todayNow);

            if (BlankUtil.isNotEmpty(todayReviewList)) {
                int size = todayReviewList.size();
                LOGGER.info("【{}】-[当天提现审核未处理的记录条数为：{}]", taskName, size);

                // 将冻结金额原路返回到余额中（插入）
                List<UmsIncome> addIncomeList = new ArrayList<>();
                // 生成一批流水ID
                List<Long> incomeIds = IdWorker.generateIds(todayReviewList.size());
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
                long partnerPrice = vo.getEarnestMoney() * vo.getProductQuantity();
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

    /**
     * 创客返还收益->
     * 逻辑:单日有没有产生创客订单,没有直接结束方法,
     * 单日用户买的创客订单,有没有推荐人,有推荐人则给这些这些推荐人执行返现,冻结收益,7天之后才可以返现,
     * 时间是以服务器上的时间为准,ums_income加上类型,冻结邀请创客收益,3000元,7天之后恢复收益到ums_income中来
     * ums_income解冻后恢复金额到结余中去,
     * 判空可以抽到公共类中去
     */
    @Transactional(rollbackFor = Exception.class)
    @Scheduled(cron = "00 05 1 * * ?")
    public void scheduledTask4() {
        LOGGER.info("异步执行邀请创客返佣开始---任务执行时间:{},线程名称:{}", LocalDateTime.now(), Thread.currentThread().getName());

        List<PidPrPdtOrderVO> vos = omsOrderService.getByPidPrPdtOrder();
        if (BlankUtil.isEmpty(vos)) {
            LOGGER.info("scheduledTask4单日没有返还给上级的分佣");
            return;
        }
        Double reMoney = settingService.getSettingValue(SettingTypeEnum.twenty);
        if (BlankUtil.isEmpty(reMoney)) {
            LOGGER.info("scheduledTask4分佣已被管理员设置关闭");
            return;
        }
        List<Long> ids = IdWorker.generateIds(vos.size());
        List<UmsIncome> incomes = new ArrayList<>();
        AtomicInteger num = new AtomicInteger();
        Integer integerOne = NumberUtils.INTEGER_ONE;
        vos.stream().filter(Objects::nonNull).forEach(
                vo -> {
                    if (BlankUtil.isNotEmpty(vo.getParentId())) {
                        num.incrementAndGet();
                        Integer quantity = vo.getProductQuantity();
                        long income = reMoney.longValue() * FigureConstant.INT_ONE_HUNDRED * quantity;
                        BigDecimal multiply = DecimalUtil.toBigDecimal(reMoney).multiply(new BigDecimal(quantity));
                        incomes.add(UmsIncome.builder()
                                             .incomeId(ids.get(num.get() - integerOne))
                                             .userId(vo.getParentId())
                                             .income(income)
                                             .expenditure(NumberUtils.LONG_ZERO)
                                             .balance(BlankUtil.isEmpty(vo.getUmsIncome()) ? NumberUtils.LONG_ZERO : (BlankUtil.isEmpty(vo
                                                     .getUmsIncome().getBalance()) ? NumberUtils.LONG_ZERO : vo
                                                     .getUmsIncome().getBalance()))
                                             .allIncome(BlankUtil.isEmpty(vo.getUmsIncome()) ? NumberUtils.LONG_ZERO : (BlankUtil.isEmpty(vo
                                                     .getUmsIncome().getAllIncome()) ? NumberUtils.LONG_ZERO : vo
                                                     .getUmsIncome().getAllIncome()))
                                             .incomeType(UmsIncome.IncomeType.TWELVE.key())
                                             .detailSource("冻结邀请用户购买创客商品:" + multiply)
                                             .remark("用户:" + vo.getUserId() + "购买创客商品返佣:" + multiply + "总计数量:" + quantity)
                                             .payType(UmsIncome.PayType.THREE.key())
                                             .incomeNo(FigureConstant.STRING_EMPTY)
                                             .orderTradeNo(FigureConstant.STRING_EMPTY)
                                             .build());
                    } else {
                        LOGGER.info("当前用户:{}没有上级用户,执行返佣为空", vo.getUserId());
                    }
                }
        );
        try {
            Optional<Long> reduce = incomes.stream().map(UmsIncome::getIncome).reduce(Long::sum);
            int save = umsIncomeService.insertBatch(incomes);
            LOGGER.info("创建创客商品推荐人返佣数量:{},返还金额,{}", save, reduce);
        } catch (Exception e) {
            LOGGER.debug("scheduledTask4系统调度信息异常,单天执行的数量为0");
            e.printStackTrace();
        }
    }

    /**
     * 七天冻结金额返还
     * 查询出来有多少给创客返佣的,不会出现多个,一个是待返的金额,一一对应的,
     */
    @Transactional(rollbackFor = Exception.class)
    @Scheduled(cron = "00 15 1 * * ?")
    public void scheduledTask5() {
        LOGGER.info("异步执行解冻创客返佣开始---任务执行时间:{},线程名称:{}", LocalDateTime.now(), Thread.currentThread().getName());

        Integer day = settingService.getSettingValue(SettingTypeEnum.twenty_one);
        if (BlankUtil.isEmpty(day)) {
            LOGGER.info("scheduledTask5管理员设置未开启邀请创客返佣");
            return;
        }
        String format = new SimpleDateFormat(com.ys.mail.model.unionPay.DateUtil.DT_SHORT_).format(cn.hutool.core.date.DateUtil.offsetDay(new Date(), day));
        List<FreezeReMoneyVO> vos = incomeService.getByFreezeReMoney(format);
        if (BlankUtil.isEmpty(vos)) {
            LOGGER.info("scheduledTask5单日没有冻结创客返佣");
            return;
        }
        List<Long> ids = IdWorker.generateIds(vos.size());
        List<UmsIncome> incomes = new ArrayList<>();
        AtomicInteger num = new AtomicInteger();
        Integer integerOne = NumberUtils.INTEGER_ONE;
        vos.stream().filter(Objects::nonNull).forEach(
                vo -> {
                    if (vo.getUserId().equals(vo.getUmsIncome().getUserId())) {
                        num.incrementAndGet();
                        BigDecimal divide = DecimalUtil.toBigDecimal(vo.getIncome())
                                                       .divide(new BigDecimal(FigureConstant.INT_ONE_HUNDRED), BigDecimal.ROUND_CEILING, RoundingMode.DOWN);
                        incomes.add(UmsIncome.builder()
                                             .incomeId(ids.get(num.get() - integerOne))
                                             .userId(vo.getUserId())
                                             .income(vo.getIncome())
                                             .expenditure(NumberUtils.LONG_ZERO)
                                             .balance(vo.getUmsIncome().getBalance() + vo.getIncome())
                                             .allIncome(vo.getUmsIncome().getAllIncome() + vo.getIncome())
                                             .incomeType(UmsIncome.IncomeType.THIRTEEN.key())
                                             .detailSource("解冻邀请创客返佣:" + divide)
                                             .incomeNo(FigureConstant.STRING_EMPTY)
                                             .orderTradeNo(FigureConstant.STRING_EMPTY)
                                             .payType(UmsIncome.PayType.THREE.key())
                                             .build());
                    }
                }
        );
        try {
            int save = umsIncomeService.insertBatch(incomes);
            LOGGER.info("创建创客商品推荐人冻结返佣数量:{},应返数量{}", vos.size(), save);
        } catch (Exception e) {
            LOGGER.debug("scheduledTask5系统调度信息异常,单天执行的数量为0");
            e.printStackTrace();
        }
    }


    /**
     * 接口单一职责性,凌晨一点二十,普通订单返还收益
     */
    @Transactional(rollbackFor = Exception.class)
    @Scheduled(cron = "00 20 1 * * ?")
    public void scheduledTask6() {

        LOGGER.info("异步执行普通订单的邀请人返佣开始---任务执行时间:{},线程名称:{}", LocalDateTime.now(), Thread.currentThread().getName());
        Double ratio = settingService.getSettingValue(SettingTypeEnum.twenty_two);
        if (BlankUtil.isEmpty(ratio)) {
            LOGGER.info("scheduledTask6管理员设置未开启邀请普通订单返佣");
            return;
        }
        List<OrdinaryReMoneyVO> vos = omsOrderService.getByOrdinaryReMoney(NumberUtils.INTEGER_ZERO);
        if (BlankUtil.isEmpty(vos)) {
            LOGGER.info("scheduledTask6单日没有普通订单返佣");
            return;
        }
        try {
            List<UmsIncome> rebate = rebate(OmsOrder.OrderType.FIVE.value(), vos, ratio);
            int save = BlankUtil.isEmpty(rebate) ? NumberUtils.INTEGER_ZERO : umsIncomeService.insertBatch(rebate);
            LOGGER.info("创建普通订单数量:{},应返数量{}", save, vos.size());
        } catch (Exception e) {
            LOGGER.debug("scheduledTask6系统调度信息异常,单天执行的数量为0");
            e.printStackTrace();
        }
    }


    /**
     * 会员订单返还收益
     */
    @Transactional(rollbackFor = Exception.class)
    @Scheduled(cron = "00 25 1 * * ?")
    public void scheduledTask7() {
        LOGGER.info("异步执行普通订单的邀请人返佣开始---任务执行时间:{},线程名称:{}", LocalDateTime.now(), Thread.currentThread().getName());
        Double ratio = settingService.getSettingValue(SettingTypeEnum.twenty_three);
        if (BlankUtil.isEmpty(ratio)) {
            LOGGER.info("scheduledTask7管理员设置未开启邀请普通订单返佣");
            return;
        }
        List<OrdinaryReMoneyVO> vos = omsOrderService.getByOrdinaryReMoney(OmsOrder.OrderType.FIVE.key());
        if (BlankUtil.isEmpty(vos)) {
            LOGGER.info("scheduledTask7单日没有会员订单返佣");
            return;
        }
        try {
            List<UmsIncome> rebate = rebate(OmsOrder.OrderType.FIVE.value(), vos, ratio);
            int save = BlankUtil.isEmpty(rebate) ? NumberUtils.INTEGER_ZERO : umsIncomeService.insertBatch(rebate);
            LOGGER.info("创建会员订单数量:{},应返数量{}", save, vos.size());
        } catch (Exception e) {
            LOGGER.debug("scheduledTask7系统调度信息异常,单天执行的数量为0");
            e.printStackTrace();
        }
    }

    /**
     * 定时检测APP信息，每隔10分钟执行一次
     */
    @Transactional(rollbackFor = Exception.class)
    @Scheduled(cron = "00 */10 * * * ?")
    public void scheduledTask8() {
        String taskName = "ScheduleTask8";
        try (CostTimeUtil ignored = new CostTimeUtil(taskName)) {
            // 获取所有APP，没有则跳过
            SqlLambdaQueryWrapper<AmsApp> wrapper = new SqlLambdaQueryWrapper<>();
            wrapper.select(AmsApp::getId);
            List<AmsApp> list = amsAppService.list(wrapper);
            // 遍历执行
            list.forEach(app -> {
                amsAppService.check(app.getId());
            });
            LOGGER.info("【{}】-[定时检测APP信息，执行条数：{}]", taskName, list.size());
        }
    }

    private List<UmsIncome> rebate(String msg, List<OrdinaryReMoneyVO> vos, Double ratio) {
        List<Long> ids = IdWorker.generateIds(vos.size());
        List<UmsIncome> incomes = new ArrayList<>();
        AtomicInteger num = new AtomicInteger();
        Integer integerOne = NumberUtils.INTEGER_ONE;
        vos.stream().filter(Objects::nonNull).forEach(
                vo -> {
                    long reMoney = new Double(vo.getTotalAmount() * ratio).longValue();
                    BigDecimal divide = DecimalUtil.toBigDecimal(reMoney)
                                                   .divide(new BigDecimal(FigureConstant.INT_ONE_HUNDRED), BigDecimal.ROUND_CEILING, RoundingMode.DOWN);
                    if (BlankUtil.isNotEmpty(vo.getParentId()) && reMoney > NumberUtils.LONG_ZERO) {
                        num.incrementAndGet();
                        incomes.add(UmsIncome.builder()
                                             .incomeId(ids.get(num.get() - integerOne))
                                             .userId(vo.getParentId())
                                             .income(reMoney)
                                             .expenditure(NumberUtils.LONG_ZERO)
                                             .balance(BlankUtil.isEmpty(vo.getUmsIncome()) ? NumberUtils.LONG_ZERO + reMoney : vo
                                                     .getUmsIncome().getBalance() + reMoney)
                                             .allIncome(BlankUtil.isEmpty(vo.getUmsIncome()) ? NumberUtils.LONG_ZERO + reMoney : vo
                                                     .getUmsIncome().getAllIncome() + reMoney)
                                             .incomeType(UmsIncome.IncomeType.THREE.key())
                                             .detailSource(msg + "返回收益" + divide)
                                             .incomeNo(FigureConstant.STRING_EMPTY)
                                             .orderTradeNo(FigureConstant.STRING_EMPTY)
                                             .remark("用户下级" + msg + "收益" + divide + " 总计数量:" + vo.getQuantity())
                                             .payType(UmsIncome.PayType.THREE.key())
                                             .build());
                    }
                }
        );
        return incomes;
    }

}
