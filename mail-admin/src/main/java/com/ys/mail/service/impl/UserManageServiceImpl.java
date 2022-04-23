package com.ys.mail.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.entity.*;
import com.ys.mail.exception.ApiException;
import com.ys.mail.mapper.*;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.dto.excel.*;
import com.ys.mail.model.admin.query.PcReviewQuery;
import com.ys.mail.model.admin.query.UmsUserQuery;
import com.ys.mail.model.admin.vo.PcReviewVO;
import com.ys.mail.model.admin.vo.UmsUserBlackListVO;
import com.ys.mail.model.admin.vo.UserImInfoVO;
import com.ys.mail.override.ChainLinkedHashMap;
import com.ys.mail.service.PcReviewService;
import com.ys.mail.service.UserManageService;
import com.ys.mail.util.*;
import com.ys.mail.wrapper.SqlLambdaQueryWrapper;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.*;


/**
 * <p>
 * app用户表 服务实现类
 * </p>
 *
 * @author 070
 * @since 2021-11-09
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class UserManageServiceImpl extends ServiceImpl<UmsUserMapper, UmsUser> implements UserManageService {

    @Autowired
    private UmsUserMapper umsUserMapper;
    @Autowired
    private OmsOrderMapper omsOrderMapper;
    @Autowired
    private UmsIncomeMapper umsIncomeMapper;
    @Autowired
    private PcReviewService pcReviewService;
    @Autowired
    private PcReviewMapper pcReviewMapper;
    @Autowired
    private SmsFlashPromotionProductMapper smsFlashPromotionProductMapper;

    @Override
    public CommonResult<IPage<UmsUserBlackListVO>> getPage(UmsUserQuery query) {
        Page<UmsUserBlackListVO> page = new Page<>(query.getPageNum(), query.getPageSize());
        String beginTime = query.getBeginTime();
        String endTime = query.getEndTime();
        QueryWrapper<UmsUserBlackListVO> wrapper = new QueryWrapper<>();
        if (BlankUtil.isNotEmpty(query.getPhone())) wrapper.like("uu.phone", query.getPhone());
        if (BlankUtil.isNotEmpty(query.getRoleId())) wrapper.eq("uu.role_id", query.getRoleId());
        if (BlankUtil.isNotEmpty(query.getUserStatus())) wrapper.eq("uu.is_user_status", query.getUserStatus());
        if (BlankUtil.isNotEmpty(query.getAlipayName())) wrapper.like("uu.alipay_name", query.getAlipayName());
        if (BlankUtil.isNotEmpty(query.getAlipayAccount())) wrapper.like("uu.alipay_account", query.getAlipayAccount());
        if (BlankUtil.isNotEmpty(query.getPaymentType())) wrapper.eq("uu.payment_type", query.getPaymentType());
        if (!BlankUtil.isEmpty(beginTime) && !BlankUtil.isEmpty(endTime)) {
            if (beginTime.compareTo(endTime) > 0) return CommonResult.failed("开始时间不能大于结束时间", null);
            wrapper.between("date_format( uu.create_time,'%Y-%m-%d %T')", beginTime, endTime);
        }
        return CommonResult.success(umsUserMapper.getPage(page, wrapper));
    }

    @Override
    public CommonResult<List<UserImInfoVO>> getUserImInfo(List<String> userIds) {
        List<UserImInfoVO> list = umsUserMapper.getUserImInfo(userIds);
        return CommonResult.success(list);
    }

    @Override
    public void exportExcel(boolean condition, HttpServletResponse response) {
        String fileName = "平台用户汇总数据";
        try (CostTimeUtil ignored = new CostTimeUtil("导出" + fileName)) {
            // 查询所有用户信息
            List<UmsUser> userList = this.list();
            if (BlankUtil.isNotEmpty(userList)) {
                // 外层包装结构
                List<Map<Long, PlatformUserCollectDTO>> pucListMapWrap = new ArrayList<>();

                // 查询用户订单汇总数据
                List<OrderCollectDTO> orderCollect = omsOrderMapper.getOrderCollect();
                // 查询用户流水汇总数据
                List<IncomeCollectDTO> incomeCollect = umsIncomeMapper.getIncomeCollect();
                // 获取用户最新余额
                List<UserBalanceDTO> userBalance = umsIncomeMapper.getUserBalance();
                // 查询用户审核汇总数据
                List<ReviewCollectDTO> reviewCollect = pcReviewMapper.getAwaitReview();
                // 查询用户秒杀汇总数据
                List<SecKillCollectDTO> secKillCollect = smsFlashPromotionProductMapper.getSecKillCollect();

                // 合并用户数据
                userList.forEach(u -> {
                    Map<Long, PlatformUserCollectDTO> pucMap = new HashMap<>(100);
                    PlatformUserCollectDTO puc = new PlatformUserCollectDTO();
                    puc.setUserId(u.getUserId());
                    puc.setUmsUser(u);
                    pucMap.put(u.getUserId(), puc);
                    pucListMapWrap.add(pucMap);
                });
                // 合并订单数据
                orderCollect.forEach(o -> {
                    PlatformUserCollectDTO puc = ListMapUtil.getListMapValue(pucListMapWrap, o.getUserId());
                    if (BlankUtil.isNotEmpty(puc)) puc.setOrderCollectDTO(o);
                });
                // 合并流水数据
                incomeCollect.forEach(i -> {
                    PlatformUserCollectDTO puc = ListMapUtil.getListMapValue(pucListMapWrap, i.getUserId());
                    if (BlankUtil.isNotEmpty(puc)) puc.setIncomeCollectDTO(i);
                });
                // 合并余额数据
                userBalance.forEach(u -> {
                    PlatformUserCollectDTO puc = ListMapUtil.getListMapValue(pucListMapWrap, u.getUserId());
                    if (BlankUtil.isNotEmpty(puc)) puc.setUserBalanceDTO(u);
                });
                // 合并审核数据
                reviewCollect.forEach(r -> {
                    PlatformUserCollectDTO puc = ListMapUtil.getListMapValue(pucListMapWrap, r.getUserId());
                    if (BlankUtil.isNotEmpty(puc)) puc.setReviewCollectDTO(r);
                });
                // 合并秒杀数据
                secKillCollect.forEach(s -> {
                    PlatformUserCollectDTO puc = ListMapUtil.getListMapValue(pucListMapWrap, s.getUserId());
                    if (BlankUtil.isNotEmpty(puc)) puc.setSecKillCollectDTO(s);
                });

                // 构造导出数据
                List<Map<String, Object>> rows = new ArrayList<>();
                pucListMapWrap.stream()
                              .sorted(Comparator.comparing(UserManageServiceImpl::comparingBySecKillConsume)
                                                .reversed()) // 降序
                              .forEach(map -> map.values().forEach(puc -> {
                                  rows.add(handleValue(puc, condition)); // 填充内容
                              }));
                // 工作表集合
                Map<String, List<Map<String, Object>>> workbookMap = new HashMap<>(1);
                workbookMap.put(fileName, rows);
                // 导出到响应流
                ExcelTool.writeExcel(workbookMap, fileName, response);
            }
        }
    }

    @Override
    public void exportUserDetailsExcel(Long userId, HttpServletResponse response) {
        String fileName = "个人明细数据";
        try (CostTimeUtil ignored = new CostTimeUtil("导出" + fileName)) {
            // 查询用户基本信息
            UmsUser umsUser = this.getById(userId);
            Optional.ofNullable(umsUser).orElseThrow(() -> new ApiException("用户ID不存在"));
            // 工作表集合
            Map<String, List<Map<String, Object>>> workbookMap = new LinkedHashMap<>(5);
            this.addUserOrderData(umsUser, workbookMap);
            this.addUserReviewData(userId, workbookMap);
            this.addUserIncomeData(userId, workbookMap);
            this.addUserSecKillData(userId, workbookMap);
            // 定义名称
            String fullName = String.format("%s-%s", umsUser.getNickname(), fileName);
            if (BlankUtil.isNotEmpty(umsUser.getAlipayName()))
                fullName = String.format("%s-%s", umsUser.getAlipayName(), fileName);
            // 导出Excel
            ExcelTool.writeExcel(workbookMap, fullName, response);
        }
    }

    private void addUserSecKillData(Long userId, Map<String, List<Map<String, Object>>> workbookMap) {
        // 查询用户所有秒杀数据
        List<UserSecKillDetailsDTO> flashDetailsData = smsFlashPromotionProductMapper.getUserFlashDetailsData(userId);
        if (BlankUtil.isNotEmpty(flashDetailsData)) {
            // 单个工作表
            List<Map<String, Object>> rows = new ArrayList<>();
            flashDetailsData.forEach(f -> {
                ChainLinkedHashMap<String, Object> map = new ChainLinkedHashMap<>();
                map.putObj("秒杀商品ID", f.getFlashPromotionPdtId().toString())
                   .putObj("秒杀场次名称", f.getFlashPromotionTitle())
                   .putObj("发布价格", f.getReleasePrice())
                   .putObj("商品原价", f.getOriginalPrice())
                   .putObj("实收金额", f.getTotalAmount())
                   .putObj("商品数量", f.getFlashPromotionCount())
                   .putObj("秒杀商品状态", EnumTool.getValue(SmsFlashPromotionProduct.FlashProductStatus.class, f.getFlashProductStatus()))
                   .putObj("客户端类型", NumberUtils.INTEGER_ZERO.equals(f.getCpyType()) ? "大尾狐" : "呼啦兔")
                   .putObj("创建时间", f.getCreateTime())
                   .putObj("更新时间", f.getUpdateTime())
                   .putObj("商品名称", f.getProductName());
                rows.add(map);
            });
            workbookMap.put("秒杀商品明细", rows);
        }
    }

    private void addUserIncomeData(Long userId, Map<String, List<Map<String, Object>>> workbookMap) {
        // 查询用户所有流水数据
        List<UmsIncome> incomeList = umsIncomeMapper.selectList(new SqlLambdaQueryWrapper<UmsIncome>().eq(UmsIncome::getUserId, userId));
        if (BlankUtil.isNotEmpty(incomeList)) {
            // 单个工作表
            List<Map<String, Object>> rows = new ArrayList<>();
            incomeList.forEach(i -> {
                ChainLinkedHashMap<String, Object> map = new ChainLinkedHashMap<>();
                map.putObj("流水号", i.getIncomeId().toString())
                   .putObj("收益/返还", NumberUtil.ifZeroReturnZero(DecimalUtil.longToDoubleForDivider(i.getIncome())))
                   .putObj("支出/扣除", NumberUtil.ifZeroReturnZero(DecimalUtil.longToDoubleForDivider(i.getExpenditure())))
                   .putObj("余额", NumberUtil.ifZeroReturnZero(DecimalUtil.longToDoubleForDivider(i.getBalance())))
                   .putObj("类型", EnumTool.getValue(UmsIncome.IncomeType.class, i.getIncomeType()))
                   .putObj("时间", i.getCreateTime())
                   .putObj("描述", i.getDetailSource())
                   .putObj("备注", i.getRemark());
                rows.add(map);
            });
            workbookMap.put("用户流水明细", rows);
        }
    }

    private void addUserReviewData(Long userId, Map<String, List<Map<String, Object>>> workbookMap) {
        // 取消分页
        PcReviewQuery query = new PcReviewQuery();
        query.setPageSize(NumberUtils.INTEGER_MINUS_ONE);
        query.setUserId(userId);
        // 查询用户所有审核数据
        CommonResult<IPage<PcReviewVO>> result = pcReviewService.getPage(query);
        // 获取记录
        List<PcReviewVO> reviewList = result.getData().getRecords();
        if (BlankUtil.isNotEmpty(reviewList)) {
            // 单个工作表
            List<Map<String, Object>> rows = new ArrayList<>();// 单个工作表
            reviewList.forEach(r -> {
                ChainLinkedHashMap<String, Object> map = new ChainLinkedHashMap<>();
                map.putObj("审核ID", r.getReviewId().toString())
                   .putObj("支付宝名字", r.getAlipayName())
                   .putObj("支付宝账号", r.getAlipayAccount())
                   .putObj("申请提现金额", DecimalUtil.longToStrForDivider(r.getReviewMoney()))
                   .putObj("审核状态", EnumTool.getValue(PcReview.ReviewState.class, r.getReviewState()))
                   .putObj("审核人ID", BlankUtil.isNotEmpty(r.getReviewId()) ? r.getReviewId().toString() : null)
                   .putObj("审核人名称", r.getUsername())
                   .putObj("提现流水号", BlankUtil.isNotEmpty(r.getIncomeId()) ? r.getIncomeId() : null)
                   .putObj("申请时间", r.getCreateTime())
                   .putObj("审核时间", r.getUpdateTime())
                   .putObj("审核描述", r.getReviewDescribe());
                rows.add(map);
            });
            workbookMap.put("提现审核明细", rows);
        }
    }

    private void addUserOrderData(UmsUser umsUser, Map<String, List<Map<String, Object>>> workbookMap) {
        // 查询用户所有订单数据
        List<UserOrderDetailsDTO> orderDetails = omsOrderMapper.getUserOrderDetailsData(umsUser.getUserId());
        if (BlankUtil.isNotEmpty(orderDetails)) {
            // 单个工作表
            List<Map<String, Object>> rows = new ArrayList<>();
            orderDetails.forEach(o -> {
                ChainLinkedHashMap<String, Object> map = new ChainLinkedHashMap<>();
                map.putObj("用户ID", umsUser.getUserId().toString())
                   .putObj("手机号", umsUser.getPhone())
                   .putObj("账号等级", NumberUtils.INTEGER_ZERO.equals(umsUser.getRoleId()) ? "普通用户" : "高级用户")
                   .putObj("会员等级", NumberUtils.INTEGER_ZERO.equals(umsUser.getPaymentType()) ? "未升级" : "已升级")
                   .putObj("支付宝名称", umsUser.getAlipayName())
                   .putObj("支付宝账号", umsUser.getAlipayAccount())
                   .putObj("订单号", o.getOrderId().toString())
                   .putObj("支付金额", o.getMoney())
                   .putObj("支付方式", EnumTool.getValue(OmsOrder.PayType.class, o.getPayType()))
                   .putObj("订单状态", EnumTool.getValue(OmsOrder.OrderStatus.class, o.getOrderStatus()))
                   .putObj("订单类型", EnumTool.getValue(OmsOrder.OrderType.class, o.getOrderType()))
                   .putObj("确认收货", NumberUtils.INTEGER_ZERO.equals(o.getConfirmStatus()) ? "未确认" : "已确认")
                   .putObj("下单时间", o.getCreateTime())
                   .putObj("支付时间", o.getPaymentTime())
                   .putObj("交易流水号", o.getTransId())
                   .putObj("客户端类型", NumberUtils.INTEGER_ZERO.equals(o.getCpyType()) ? "大尾狐" : "呼啦兔")
                   .putObj("订单备注", o.getOrderNote())
                   .putObj("商品名称", o.getProductName());
                rows.add(map);
            });
            workbookMap.put("用户订单明细", rows);
        }
    }

    /**
     * 处理数据
     *
     * @param puc       平台用户数据
     * @param condition 条件：false->简单版，true->详细版
     * @return 行结果
     */
    private Map<String, Object> handleValue(PlatformUserCollectDTO puc, boolean condition) {
        ChainLinkedHashMap<String, Object> chainMap = new ChainLinkedHashMap<>();
        // 用户订单数据
        OrderCollectDTO order = puc.getOrderCollectDTO();
        if (BlankUtil.isEmpty(order)) order = new OrderCollectDTO();
        // 用户流水数据
        IncomeCollectDTO income = puc.getIncomeCollectDTO();
        if (BlankUtil.isEmpty(income)) income = new IncomeCollectDTO();
        // 用户当天审核数据
        ReviewCollectDTO reviewCollectDTO = puc.getReviewCollectDTO();
        if (BlankUtil.isEmpty(reviewCollectDTO)) reviewCollectDTO = new ReviewCollectDTO();
        // 用户秒杀数据
        SecKillCollectDTO secKill = puc.getSecKillCollectDTO();
        if (BlankUtil.isEmpty(secKill)) secKill = new SecKillCollectDTO();
        // 当前最新余额
        UserBalanceDTO userBalance = puc.getUserBalanceDTO();
        if (BlankUtil.isEmpty(userBalance)) userBalance = new UserBalanceDTO();
        // 用户基本数据
        UmsUser umsUser = puc.getUmsUser();

        // 读取值或初始值
        double balanceEx = NumberUtil.ifNullDefaultValue(income.getBalanceEx());
        double reviewDeduct = NumberUtil.ifNullDefaultValue(income.getReviewDeduct());
        double reviewRefund = NumberUtil.ifNullDefaultValue(income.getReviewRefund());
        double teamIncome = NumberUtil.ifNullDefaultValue(income.getTeamIncome());
        double balancePay = NumberUtil.ifNullDefaultValue(income.getBalancePay());
        double generalConsume = NumberUtil.ifNullDefaultValue(order.getGeneralConsume());
        double secKillConsume = NumberUtil.ifNullDefaultValue(order.getSecKillConsume());
        double refunded = NumberUtil.ifNullDefaultValue(order.getRefunded());
        double waitTake = NumberUtil.ifNullDefaultValue(order.getWaitTake());
        double reviewMoney = NumberUtil.ifNullDefaultValue(reviewCollectDTO.getReviewMoney());
        double sumIncome = NumberUtil.ifNullDefaultValue(secKill.getSumIncome());
        double sumSell = NumberUtil.ifNullDefaultValue(secKill.getSumSell());
        double serviceChargeDeduct = NumberUtil.ifNullDefaultValue(income.getServiceChargeDeduct());
        double serviceChargeRefund = NumberUtil.ifNullDefaultValue(income.getServiceChargeRefund());
        double currentBalance = NumberUtil.ifNullDefaultValue(userBalance.getCurrentBalance());
        double sumConsume = NumberUtil.ifNullDefaultValue(order.getSumConsume());
        String payList = order.getPayList();
        if (BlankUtil.isNotEmpty(payList) && payList.startsWith(",")) payList = payList.substring(1);

        // =================================【 计算公式 】=================================
        // 已提现 = (直接提现 + 审核提现) - 审核退还 - 当天待审核
        double received = balanceEx + reviewDeduct - reviewRefund - reviewMoney;
        // 实际消费 = 总消费 - 余额支付(内部消费，不算转入)
        double actualConsume = sumConsume - balancePay;
        // 分佣 = 秒杀消费 * 分佣比例 TODO:此处需要读取配置
        double maxSecKillIncome = secKillConsume * 0.01;
        double minSecKillIncome = secKillConsume * 0.005;
        // 提现服务费 = 扣除服务费 - 退还服务费
        double serviceTip = serviceChargeDeduct - serviceChargeRefund;
        // 预计卖出收益 = sumIncome + 待收货*1.005
        double saleSumIncome = sumIncome + waitTake * 1.005;
        // 预计总余额 = 待审核 + 账户余额 + 预计卖出收益 + TODO：待审核手续费
        double predictBalance = reviewMoney + currentBalance + saleSumIncome;
        // 预计总资产 = 秒杀消费(包含余额支付) + 秒杀分佣 + 团长分佣
        double maxPredictAsset = secKillConsume + maxSecKillIncome + teamIncome;
        double minPredictAsset = secKillConsume + minSecKillIncome + teamIncome;
        // 实际总资产 = 预计总余额 + 已提现 + 提现服务费(花费出去)
        double actualAsset = predictBalance + received + serviceTip;
        // 实际盈利 = 实际总资产 - (秒杀消费 - 余额支付)
        double actualProfit = actualAsset - (secKillConsume - balancePay);
        // 差值 = 实际总资产 - 最大总资产 + 余额支付
        double maxDiffValue = actualAsset - maxPredictAsset + balancePay;
        double mixDiffValue = actualAsset - minPredictAsset + balancePay;
        // =================================【 End 】=================================

        // 填充模板数据
        paddingTemplate(condition, puc, umsUser, chainMap, order, income, secKill, teamIncome, balancePay, generalConsume, secKillConsume, refunded, waitTake, reviewMoney, sumSell, currentBalance, sumConsume, payList, received, actualConsume, maxSecKillIncome, minSecKillIncome, serviceTip, saleSumIncome, predictBalance, maxPredictAsset, minPredictAsset, actualAsset, actualProfit, maxDiffValue, mixDiffValue);

        return chainMap;
    }

    /**
     * 填充模板数据
     *
     * @param condition 条件
     */
    private void paddingTemplate(boolean condition, PlatformUserCollectDTO puc, UmsUser umsUser, ChainLinkedHashMap<String, Object> chainMap, OrderCollectDTO order, IncomeCollectDTO income, SecKillCollectDTO secKill, double teamIncome, double balancePay, double generalConsume, double secKillConsume, double refunded, double waitTake, double reviewMoney, double sumSell, double currentBalance, double sumConsume, String payList, double received, double actualConsume, double maxSecKillIncome, double minSecKillIncome, double serviceTip, double saleSumIncome, double predictBalance, double maxPredictAsset, double minPredictAsset, double actualAsset, double actualProfit, double maxDiffValue, double mixDiffValue) {

        chainMap.putObj("用户ID", puc.getUserId().toString()).putObj("手机号", umsUser.getPhone())
                .putObj("支付宝名称", umsUser.getAlipayName()).putObj("支付宝账号", umsUser.getAlipayAccount())
                .putObj("下单支付账号列表", payList)
                .putObj("注册时间", umsUser.getCreateTime(), condition)
                .putObj("最近下单支付时间", order.getLatestPaymentTime(), condition)
                // 最近提现时间
                .putObj("普通消费", NumberUtil.ifZeroReturnZero(generalConsume))
                .putObj("已退款", NumberUtil.ifZeroReturnZero(refunded), condition)
                .putObj("未退款", NumberUtil.ifZeroReturnZero(generalConsume - refunded))
                .putObj("会员消费", NumberUtil.ifZeroReturnZero(order.getMemberConsume()), condition)
                .putObj("创客消费", NumberUtil.ifZeroReturnZero(order.getMakerConsume()), condition)
                .putObj("待收货", NumberUtil.ifZeroReturnZero(waitTake), condition)
                .putObj("待秒杀", NumberUtil.ifZeroReturnZero(secKill.getWaitSecKill()), condition)
                .putObj("已上架", NumberUtil.ifZeroReturnZero(secKill.getWaitPutAway()), condition)
                .putObj("待卖出", NumberUtil.ifZeroReturnZero(secKill.getWaitSell()), condition)
                .putObj("总未卖", NumberUtil.ifZeroReturnZero(sumSell + waitTake))
                .putObj("预计卖出收益", NumberUtil.ifZeroReturnZero(saleSumIncome))
                .putObj("秒杀流水", NumberUtil.ifZeroReturnZero(income.getSecKillIncome()), condition)
                .putObj("商家收益", NumberUtil.ifZeroReturnZero(income.getMerchantIncome()), condition)
                .putObj("创客收益", NumberUtil.ifZeroReturnZero(income.getMakerIncome()), condition)
                .putObj("提现服务费", NumberUtil.ifZeroReturnZero(serviceTip))
                .putObj("当天小额提现", NumberUtil.ifZeroReturnZero(income.getSmallAmount()))
                .putObj("当天待审核", NumberUtil.ifZeroReturnZero(reviewMoney))
                .putObj("账户余额", NumberUtil.ifZeroReturnZero(currentBalance))
                .putObj("预计总余额", NumberUtil.ifZeroReturnZero(predictBalance))
                .putObj("预计最大秒杀分佣", NumberUtil.ifZeroReturnZero(maxSecKillIncome))
                .putObj("预计最小秒杀分佣", NumberUtil.ifZeroReturnZero(minSecKillIncome))
                .putObj("团长分佣", NumberUtil.ifZeroReturnZero(teamIncome))
                .putObj("预计最大总资产", NumberUtil.ifZeroReturnZero(maxPredictAsset), condition)
                .putObj("预计最小总资产", NumberUtil.ifZeroReturnZero(minPredictAsset), condition)
                .putObj("秒杀消费", NumberUtil.ifZeroReturnZero(secKillConsume))
                .putObj("总消费", NumberUtil.ifZeroReturnZero(sumConsume))
                .putObj("余额支付", NumberUtil.ifZeroReturnZero(balancePay))
                .putObj("实际消费", NumberUtil.ifZeroReturnZero(actualConsume))
                .putObj("已提现", NumberUtil.ifZeroReturnZero(received))
                .putObj("实际总资产", NumberUtil.ifZeroReturnZero(actualAsset))
                .putObj("实际盈利", NumberUtil.ifZeroReturnZero(actualProfit))
                .putObj("最大差值", NumberUtil.ifZeroReturnZero(maxDiffValue))
                .putObj("最小差值", NumberUtil.ifZeroReturnZero(mixDiffValue));

        // 添加额外列
        chainMap.putObj("实际转入", null).putObj("实际转出", null).putObj("转入与消费", null).putObj("转出与提现", null)
                .putObj("实际余额", null).putObj("实际亏损", null).putObj("备注", null);
    }

    /**
     * 比较排序器
     *
     * @param map 对象
     * @return 返回金额
     */
    private static Double comparingBySecKillConsume(Map<Long, PlatformUserCollectDTO> map) {
        for (PlatformUserCollectDTO puc : map.values()) {
            OrderCollectDTO orderCollectDTO = puc.getOrderCollectDTO();
            if (BlankUtil.isNotEmpty(orderCollectDTO)) return orderCollectDTO.getSecKillConsume();
        }
        return NumberUtils.DOUBLE_ZERO;
    }
}
