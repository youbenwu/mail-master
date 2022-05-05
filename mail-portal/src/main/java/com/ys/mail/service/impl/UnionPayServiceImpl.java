package com.ys.mail.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ys.mail.config.RabbitMqSmsConfig;
import com.ys.mail.constant.AlipayConstant;
import com.ys.mail.constant.FigureConstant;
import com.ys.mail.constant.UnionPayConstant;
import com.ys.mail.entity.OmsOrder;
import com.ys.mail.entity.UmsIncome;
import com.ys.mail.entity.UmsUser;
import com.ys.mail.exception.ApiException;
import com.ys.mail.exception.code.BusinessErrorCode;
import com.ys.mail.exception.code.CommonResultCode;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.dto.ChangeActiveDto;
import com.ys.mail.model.param.AliBuyProductParam;
import com.ys.mail.model.param.BalanceProductParam;
import com.ys.mail.model.param.BuyProductParam;
import com.ys.mail.model.unionPay.DateUtil;
import com.ys.mail.model.unionPay.HttpClientUtil;
import com.ys.mail.model.unionPay.SignUtils;
import com.ys.mail.model.unionPay.WebPay;
import com.ys.mail.model.vo.OrderItemSkuVO;
import com.ys.mail.service.*;
import com.ys.mail.util.*;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.BadPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author DT
 * @version 1.0
 * @date 2021-12-08 14:53
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class UnionPayServiceImpl implements UnionPayService {

    private final static Logger LOGGER = LoggerFactory.getLogger(UnionPayServiceImpl.class);

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 秒*分钟数,过期时间5分钟
     */
    private static final int END_TIME = 300;

    @Autowired
    private OmsOrderService orderService;
    @Autowired
    private UmsUserInviteService inviteService;
    @Autowired
    private UmsIncomeService incomeService;
    @Autowired
    private OmsOrderItemService orderItemService;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UmsUserService userService;
    @Autowired
    private PmsProductService productService;

    @Value("${prop.staticAccessPath}")
    private String accessPath;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommonResult<Boolean> balancePaymentBuyProduct(BalanceProductParam param) throws AlipayApiException {
        LOGGER.info("余额支付开始,param:{}", JSON.toJSONString(param));
        //防止超卖
        BusinessErrorCode repBuyProduct = repBuyProduct(param.getOrderSn(), param.getAmount());
        if (repBuyProduct.getCode() != BusinessErrorCode.RESPONSE_SUCCESS.getCode()) {
            return CommonResult.failed(repBuyProduct);
        }
        // 日志模板
        List<String> template = new ArrayList<>();
        template.add("【余额支付日志】==> [ID：{},昵称：{}]的用户发起余额支付，预计金额为：[{}]元");
        template.add("【余额支付日志】==> [ID：{},昵称：{}]的用户发起余额支付，失败原因：{}");
        // 校验支付密码与支付名称
        UmsUser user = userService.getById(param.getUserId());
        if (BlankUtil.isEmpty(user)) {
            return CommonResult.failed(CommonResultCode.ERROR_20001);
        }
        Long userId = user.getUserId();
        String nickname = user.getNickname();
        String alipayName = param.getAlipayName();
        if (BlankUtil.isEmpty(user.getPayPassword())) {
            LOGGER.info(template.get(1), userId, nickname, CommonResultCode.ERR_TEM_PAY_CODE.getMessage());
            return CommonResult.failed(CommonResultCode.ERR_TEM_PAY_CODE);
        } else if (!passwordEncoder.matches(param.getPayPassword(), user.getPayPassword())) {
            LOGGER.info(template.get(1), userId, nickname, CommonResultCode.ERR_NOT_PAY_CODE.getMessage());
            return CommonResult.failed(CommonResultCode.ERR_NOT_PAY_CODE);
        } else if (!user.getAlipayName().equals(alipayName)) {
            LOGGER.info(template.get(1), userId, nickname, BusinessErrorCode.ERR_NAME_ALIPAY.getMessage());
            return CommonResult.failed(BusinessErrorCode.ERR_NAME_ALIPAY);
        }
        //查询用户余额
        UmsIncome income = incomeService.selectNewestByUserId(userId);
        if (BlankUtil.isEmpty(income)) {
            return CommonResult.failed(CommonResultCode.ERROR_20002);
        }
        Long balance = income.getBalance(); // 当前余额 5000 00
        Long money = DecimalUtil.strToLongForMultiply(param.getAmount()); // 余额，乘以100


        // 系统订单号
        String orderSn = param.getOrderSn();
        OmsOrder order = orderService.getOne(new QueryWrapper<OmsOrder>().eq("order_sn", orderSn));
        if (BlankUtil.isEmpty(order)) {
            return CommonResult.failed(CommonResultCode.ERROR_20003);
        }
        boolean rep;
        //验证余额是否足够付款
        if (balance < money) {
            return CommonResult.failed(CommonResultCode.ERR_USER_DEPOSIT);
        } else {
            //更新订单状态
            // 交易状态--可抽公用
            order.setOrderStatus(OmsOrder.OrderStatus.ONE.key());
            order.setPaymentTime(new Date());
            order.setPayType(3);
            //rep = orderService.updateById(order);
            orderService.updateById(order);
            long finalBalance = balance - money; // 操作成功最终的余额
            //保存交易流水信息--可抽公用
            UmsIncome umsIncome = UmsIncome.builder()
                                           .incomeId(IdWorker.generateId())
                                           .userId(userId)
                                           .income(NumberUtils.LONG_ZERO)
                                           .expenditure(money)
                                           .balance(finalBalance)
                                           .todayIncome(income.getTodayIncome())
                                           .allIncome(income.getAllIncome())
                                           .incomeType(7) // 7->余额支付购买
                                           .incomeNo("")
                                           .orderTradeNo("")
                                           .detailSource("余额支付购买:" + money + "元")
                                           .payType(3)
                                           .build();
            rep = incomeService.save(umsIncome);
        }
        LOGGER.info("余额支付结束,result:{}", rep);
        return rep ? CommonResult.success(rep) : CommonResult.failed(false);
    }

    @Override
    public synchronized String notifyUrl(HttpServletRequest request, HttpServletResponse response) {

        LOGGER.info("接收回调地址开始");
        Map<String, String> reqParam = getAllRequestParam(request);
        // 签名验证(对云闪付返回的数据验证,确定是云闪付返回的)
        boolean signVerified = false;
        boolean rep = false;

        if (!BlankUtil.isEmpty(reqParam) && reqParam.size() == FigureConstant.INT_SIX) {
            // 根据商户号切换不同的公司大尾狐和呼啦兔
            String signContent = WebPay.getSignContent(reqParam);
            String account = reqParam.get("account");
            String pubKey = account.equals(UnionPayConstant.JIH_ACCOUNT) ?
                    UnionPayConstant.JIU_PUB_KEY : UnionPayConstant.JUH_PUB_KEY;

            // 调用云闪付公钥进行签名
            try {
                signVerified = SignUtils.verifySign(pubKey, signContent, SignUtils.sign(UnionPayConstant.JIU_SIGN_KEY, signContent));
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.info("验签失败:{}", e.getMessage());
            }

            if (signVerified) {
                // 交易状态
                String result = reqParam.get("result");
                // 交易金额 整数类型*100
                String amount = reqParam.get("amount");
                // 云闪付商户订单号 做对账用
                String transId = reqParam.get("trans_id");
                // 系统订单号
                String orderSn = reqParam.get("app_id");

                // 对比交易查询,对比金额
                OmsOrder order = orderService.getOne(new QueryWrapper<OmsOrder>().eq("order_sn", orderSn));
                if (!BlankUtil.isEmpty(order) && order.getTotalAmount().toString().equals(amount)) {

                    // 金额一致进入方法,异步回调没有返回支付时间,那就只能new一个了
                    switch (result) {
                        default:
                        case UnionPayConstant.RESULT_SUCCESS:
                            order.setOrderStatus(OmsOrder.OrderStatus.ONE.key());
                            break;
                        case UnionPayConstant.RESULT_ERROR:
                            order.setOrderStatus(OmsOrder.OrderStatus.TWO.key());
                            break;
                        case UnionPayConstant.RESULT_UNPAID:
                            order.setOrderStatus(OmsOrder.OrderStatus.ZERO.key());
                            break;
                        case UnionPayConstant.RESULT_LOST:
                            order.setOrderStatus(OmsOrder.OrderStatus.FIVE.key());
                            break;
                        case UnionPayConstant.RESULT_REFUND:
                            order.setOrderStatus(OmsOrder.OrderStatus.FOUR.key());
                            break;
                    }
                    order.setPaymentTime(new Date());
                    order.setTransId(transId);
                    order.setPayType(NumberUtils.INTEGER_ONE);
                    Byte cpyType = account.equals(UnionPayConstant.JIH_ACCOUNT) ? NumberUtils.BYTE_ZERO : NumberUtils.BYTE_ONE;
                    order.setCpyType(cpyType);
                    order.setPayType(NumberUtils.INTEGER_TWO);
                    rep = orderService.updateById(order);
                }
            }
        }
        LOGGER.info("云闪付接收回调结果");
        return rep ? UnionPayConstant.SUCCESS : UnionPayConstant.ERROR;
    }

    @Override
    public synchronized CommonResult<JSONObject> buyProduct(BuyProductParam param) {
        // TODO 支付中再查一下库存够不,防止超卖
        OrderItemSkuVO vo = orderItemService.getByOrderSn(param.getOrderSn());
        if (BlankUtil.isEmpty(vo) || vo.getProductQuantity() > vo.getStock()) {
            return CommonResult.failed(BusinessErrorCode.GOODS_STOCK_EMPTY);
        } else if (!param.getAmount().equals(vo.getProductPrice())) {
            return CommonResult.failed(BusinessErrorCode.ERR_PRODUCT_PRICE);
        }
        String repStr = repUnionPay(param);
        if (BlankUtil.isEmpty(repStr)) {
            return CommonResult.failed(BusinessErrorCode.ERR_UNION_E_MSG);
        }
        JSONObject jsonObject = new JSONObject();
        JSONObject result = JSONObject.parseObject(repStr, Feature.OrderedField);
        boolean equals = result.get(UnionPayConstant.ERROR_CODE).equals(UnionPayConstant.CODE);
        if (equals) {
            // 回调等用户支付完再改状态
            JSONObject response = JSONObject.parseObject(result.get("response").toString());
            jsonObject.put(UnionPayConstant.ERROR_CODE, UnionPayConstant.CODE);
            jsonObject.put("errormessage", result.get("errormessage"));
            jsonObject.put("pay_url", response.get("pay_url"));
        }
        orderService.updateByCpyType(param.getOrderSn(), param.getCpyType());
        return equals ? CommonResult.success(jsonObject) : CommonResult.failed(result);
    }

    /**
     * 获取参数集合
     *
     * @param request 参数
     * @return 返回值
     */
    public static Map<String, String> getAllRequestParam(
            final HttpServletRequest request) {
        Map<String, String> res = new HashMap<String, String>(FigureConstant.MAP_INIT_SIX_EEN);
        Enumeration<?> temp = request.getParameterNames();
        if (null != temp) {
            while (temp.hasMoreElements()) {
                String en = (String) temp.nextElement();
                String value = request.getParameter(en);
                res.put(en, value);
                // 在报文上送时，如果字段的值为空，则不上送<下面的处理为在获取所有参数数据时，判断若值为空，则删除这个字段>
                if (res.get(en) == null || "".equals(res.get(en))) {
                    System.out.println("======为空的字段名====" + en);
                    res.remove(en);
                }
            }
        }
        return res;
    }

    private String repUnionPay(BuyProductParam param) {

        LOGGER.info("开始封装实体");
        Map<String, String> paramsMap = new HashMap<>(FigureConstant.MAP_INIT_SIX_EEN);
        String sysCode = param.getCpyType().equals(NumberUtils.BYTE_ZERO) ?
                UnionPayConstant.JIH_SYS_CODE : UnionPayConstant.JUH_SYS_CODE;
        String account = param.getCpyType().equals(NumberUtils.BYTE_ZERO) ?
                UnionPayConstant.JIH_ACCOUNT : UnionPayConstant.JUH_ACCOUNT;
        paramsMap.put("syscode", sysCode);
        paramsMap.put("account", account);
        paramsMap.put("trans_time", new SimpleDateFormat(DateUtil.DT_LONG).format(new Date()));
        paramsMap.put("amount", param.getAmount());
        String payMode = null;
        switch (param.getPayType()) {
            default:
            case FigureConstant.BYTE_ZERO:
                payMode = UnionPayConstant.PAY_MODE_API_CU_APP;
                break;
            case FigureConstant.BYTE_ONE:
                payMode = UnionPayConstant.PAY_MODE_API_WX_APP;
                break;
            case FigureConstant.BYTE_TWO:
                payMode = UnionPayConstant.PAY_MODE_API_ZFB_APP;
                break;
        }
        paramsMap.put("pay_mode", payMode);
        paramsMap.put("app_id", param.getOrderSn());
        paramsMap.put("notify_url", UnionPayConstant.NOTIFY_URL);
        paramsMap.put("charset", UnionPayConstant.CHARSET);
        String memo = param.getCpyType().equals(NumberUtils.BYTE_ZERO) ?
                UnionPayConstant.JIU_USER_BUY_PRODUCT : UnionPayConstant.JUH_USER_BUY_PRODUCT;
        paramsMap.put("memo", memo);

        String signData = WebPay.getSignContent(paramsMap);
        String sing = param.getCpyType().equals(NumberUtils.BYTE_ZERO) ?
                SignUtils.sign(UnionPayConstant.JIU_SIGN_KEY, signData) : SignUtils.sign(UnionPayConstant.JUH_SIGN_KEY, signData);
        paramsMap.put("sign", sing);
        String respStr = HttpClientUtil.sendPost(UnionPayConstant.URL, paramsMap);
        LOGGER.info("响应报文{}", respStr);
        return respStr;
    }

    @Override
    public synchronized CommonResult<String> aliPayBuyProduct(AliBuyProductParam param) throws AlipayApiException {
//        BusinessErrorCode repBuyProduct = repBuyProduct(param.getOrderSn(), param.getAmount());
//        if (repBuyProduct.getCode() != BusinessErrorCode.RESPONSE_SUCCESS.getCode()) {
//            return CommonResult.failed(repBuyProduct);
//        }
        AlipayTradeAppPayResponse response = repAlipay(param);
        String body = response.getBody();
        return response.isSuccess() ? CommonResult.success(body) : CommonResult.failed(body);
    }

    @Override
    public CommonResult<String> aliPayBuyUpgrade(AliBuyProductParam param) throws AlipayApiException {
//        BussinessErrorCode repBuyProduct = repBuyProduct(param.getOrderSn(), param.getAmount());
//        if(repBuyProduct.getCode() != BussinessErrorCode.RESPONSE_SUCCESS.getCode()){
//            return CommonResult.failed(repBuyProduct);
//        }
        AlipayTradeAppPayResponse response = repAlipay(param);
        String body = response.getBody();
        return response.isSuccess() ? CommonResult.success(body) : CommonResult.failed(body);
    }

    private BusinessErrorCode repBuyProduct(String orderSn, String amount) {
        // TODO 支付中再查一下库存够不,防止超卖
        OrderItemSkuVO vo = orderItemService.getByOrderSn(orderSn);
        BusinessErrorCode resultEmpty = BusinessErrorCode.RESPONSE_SUCCESS;
        if (BlankUtil.isEmpty(vo) || vo.getProductQuantity() > vo.getStock()) {
            resultEmpty = BusinessErrorCode.GOODS_STOCK_EMPTY;
        } else if (!amount.equals(vo.getProductPrice())) {
            resultEmpty = BusinessErrorCode.ERR_PRODUCT_PRICE;
        }
        return resultEmpty;
    }

    private AlipayTradeAppPayResponse repAlipay(AliBuyProductParam param) throws AlipayApiException {
        // 待和前端联调,应用还未审核成功待联调
        //构造client
        CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
        //设置网关地址
        certAlipayRequest.setServerUrl(AlipayConstant.SERVER_URL);
        // 因为吉虎收款被限制,所以换成都是吉狐收款
        String subject = "";
        Byte cpyType = NumberUtils.BYTE_ZERO;
        switch (cpyType) {
            default:
            case FigureConstant.BYTE_ZERO:
                //设置应用Id
                certAlipayRequest.setAppId(AlipayConstant.JIU_APP_ID);
                //设置应用私钥
                certAlipayRequest.setPrivateKey(AlipayConstant.JIU_APP_PRIVATE_KEY);
                //设置应用公钥证书路径,暂时先不优化
                certAlipayRequest.setCertPath(new File(accessPath + AlipayConstant.JIU_APP_CERT_PATH).getPath());
                //设置支付宝公钥证书路径
                certAlipayRequest.setAlipayPublicCertPath(new File(accessPath + AlipayConstant.JIU_ALIPAY_CERT_PATH).getPath());
                //设置支付宝根证书路径
                certAlipayRequest.setRootCertPath(new File(accessPath + AlipayConstant.JIU_ALIPAY_ROOT_CERT_PATH).getPath());
                //设置支付宝订单描述语
                subject = AlipayConstant.JIU_SUBJECT;
                break;
            case FigureConstant.BYTE_ONE:
                certAlipayRequest.setAppId(AlipayConstant.JH_YF_APP_ID);
                certAlipayRequest.setPrivateKey(AlipayConstant.JH_YF_APP_PRIVATE_KEY);
                certAlipayRequest.setCertPath(new File(accessPath + AlipayConstant.JH_YF_APP_CERT_PATH).getPath());
                certAlipayRequest.setAlipayPublicCertPath(new File(accessPath + AlipayConstant.JH_YF_ALIPAY_CERT_PATH).getPath());
                certAlipayRequest.setRootCertPath(new File(accessPath + AlipayConstant.JH_YF_ALIPAY_ROOT_CERT_PATH).getPath());
                subject = AlipayConstant.JH_YF_SUBJECT;
                break;
        }

        //设置请求格式，固定值json
        certAlipayRequest.setFormat(AlipayConstant.FORMAT);
        //设置字符集
        certAlipayRequest.setCharset(AlipayConstant.CHARSET);
        //设置签名类型
        certAlipayRequest.setSignType(AlipayConstant.SIGN_TYPE);

        //构造client
        AlipayClient alipayClient = new DefaultAlipayClient(certAlipayRequest);

        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        request.setNotifyUrl(AlipayConstant.NOTIFY_URL);
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", param.getOrderSn());
        BigDecimal totalAmount = DecimalUtil.toBigDecimal(param.getAmount()).divide(new BigDecimal(100));
        bizContent.put("total_amount", totalAmount);
        bizContent.put("subject", subject);
        bizContent.put("product_code", AlipayConstant.PRODUCT_CODE_PAY);
        bizContent.put("timeout_express", AlipayConstant.TIMEOUT_EXPRESS);
        request.setBizContent(bizContent.toString());

        return alipayClient.sdkExecute(request);
    }

    public String repNotify(Map<String, String> conversionParams) {
        LOGGER.info("----支付宝异步请求逻辑处理---");

        // 签名验证(对支付宝返回的数据验证,确定是支付宝返回的)
        boolean signVerified = false;
        boolean response = true;

        //调用SDK验证签名,后面好拓展
        try {
            String path = "";
            switch (conversionParams.get("app_id")) {
                default:
                case AlipayConstant.JIU_APP_ID:
                    path = new File(accessPath + AlipayConstant.JIU_ALIPAY_CERT_PATH).getPath();
                    break;
                case AlipayConstant.JH_YF_APP_ID:
                    path = new File(accessPath + AlipayConstant.JH_YF_ALIPAY_CERT_PATH).getPath();
                    break;
            }
            signVerified = AlipaySignature.rsaCertCheckV1(conversionParams,
                    path,
                    AlipayConstant.CHARSET,
                    AlipayConstant.SIGN_TYPE);
        } catch (AlipayApiException e) {
            e.printStackTrace();
            LOGGER.debug("验签失败:{}", e.getMessage());
        } catch (RuntimeException e) {
            e.printStackTrace();
            LOGGER.debug("其它异常:{}", e.getMessage());
        }

        if (signVerified) {
            //获取商户之前传给支付宝的订单号（商户系统的唯一订单号）
            String outTradeNo = conversionParams.get("out_trade_no");
            //订单金额:本次交易支付的订单金额，单位为人民币（元）
            String totalAmount = conversionParams.get("total_amount");
            //交易付款时间
            String gmtPayment = conversionParams.get("gmt_payment");
            //支付宝的交易号
            String tradeNo = conversionParams.get("trade_no");
            // 获取交易状态
            String tradeStatus = conversionParams.get("trade_status");
            //买家支付宝账号
            String buyerLogonId = conversionParams.get("buyer_logon_id");

            //支付宝官方建议校验的值（out_trade_no、total_amount、sellerId、app_id,查出订单信息

            // 查出订单信息
            OmsOrder order = orderService.getOne(new QueryWrapper<OmsOrder>().eq("order_sn", outTradeNo));
            BigDecimal multiply = DecimalUtil.toBigDecimal(totalAmount)
                                             .multiply(new BigDecimal(FigureConstant.INT_ONE_HUNDRED));
            if (!BlankUtil.isEmpty(order) && !BlankUtil.isEmpty(multiply)
                    && multiply.compareTo(DecimalUtil.toBigDecimal(order.getPayAmount())) == NumberUtils.INTEGER_ZERO) {
                try {
                    response = this.handleCallback(order, tradeStatus, outTradeNo, tradeNo, gmtPayment);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                order.setTransId(tradeNo);
                order.setPayType(NumberUtils.INTEGER_TWO);
                order.setBuyerLogonId(buyerLogonId);
            }

            if (response && BlankUtil.isNotEmpty(order) && BlankUtil.isNotEmpty(order.getOrderId())) {
                response = orderService.updateById(order);
            }

        }
        LOGGER.info("支付宝异步请求逻辑结束:{}", response);
        return response ? AlipayConstant.SUCCESS : AlipayConstant.FAIL;
    }

    /**
     * 统一回调业务处理
     *
     * @param order       订单
     * @param tradeStatus 交易状态
     * @param outTradeNo  商户订单号
     * @param tradeNo     支付宝交易号
     * @param gmtPayment  交易付款时间
     * @return 是否执行成功
     * @throws ParseException e
     */
    private boolean handleCallback(OmsOrder order, String tradeStatus, String outTradeNo, String tradeNo, String gmtPayment) throws ParseException {
        boolean response = true;
        switch (tradeStatus) {
            default:
            case AlipayConstant.TRADE_SUCCESS:
                switch (EnumTool.getEnum(OmsOrder.OrderType.class, order.getOrderType())) {
                    case ONE:
                        order.setOrderStatus(OmsOrder.OrderStatus.TWO.key());
                        rabbitTemplate.convertAndSend(RabbitMqSmsConfig.CHANGE_ACTIVE_EXCHANGE,
                                RabbitMqSmsConfig.CHANGE_ACTIVE_ROUTING_KEY,
                                new ChangeActiveDto(order.getCreateTime(),
                                        order.getUserId(), outTradeNo));
                        break;
                    case TWO:
                        LOGGER.info("拼团订单");
                        break;
                    case THREE:
                        LOGGER.info("【升级高级会员付款回调】,流水号：{}", tradeNo);
                        order.setOrderStatus(OmsOrder.OrderStatus.SIX.key());
                        response = orderService.updateUserForSeniorPay(order.getUserId(), order);
                        break;
                    case FOUR:
                        order.setOrderStatus(OmsOrder.OrderStatus.SIX.key());
                        response = productService.updateByOrderId(order.getOrderId());
                        break;
                    case ZERO:
                    default:
                        order.setOrderStatus(OmsOrder.OrderStatus.ONE.key());
                }
                break;
            case AlipayConstant.TRADE_CLOSED:
                order.setOrderStatus(OmsOrder.OrderStatus.FOUR.key());
                break;
        }
        order.setPaymentTime(SDF.parse(gmtPayment));
        return response;
    }

    @Override
    public synchronized String aliPayNotify(HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info("----支付宝异步获取参数开始---");

        Map<String, String> retMap = new HashMap<>(FigureConstant.MAP_INIT_SIX_EEN);
        Set<Map.Entry<String, String[]>> entrySet = request.getParameterMap().entrySet();
        for (Map.Entry<String, String[]> entry : entrySet) {
            String name = entry.getKey();
            String[] values = entry.getValue();
            int valLen = values.length;

            if (valLen == NumberUtils.INTEGER_ONE) {
                retMap.put(name, values[NumberUtils.INTEGER_ZERO]);
            } else if (valLen > NumberUtils.INTEGER_ONE) {
                StringBuilder sb = new StringBuilder();
                for (String val : values) {
                    sb.append(",").append(val);
                }
                retMap.put(name, sb.substring(NumberUtils.INTEGER_ONE));
            } else {
                retMap.put(name, "");
            }
        }
        LOGGER.info("支付宝异步获取参数结束:{}", retMap);
        return repNotify(retMap);
    }

    @Override
    public synchronized CommonResult<String> balancePaySign(AliBuyProductParam param) throws Exception {
        // 生成签名,一串字符串,一些用户的基本信息,一个订单号,和一个金额,过期时间5分钟过期
        JSONObject result = new JSONObject();
        String sign = null;
        try {
            result.put("sn", param.getOrderSn());
            result.put("a", param.getAmount());
            result.put("id", UserUtil.getCurrentUser().getUserId());
            result.put("t", DateUtil.addSeconds(new Date(), END_TIME));
            sign = RasUtil.encrypt(result.toJSONString(), RasUtil.getPublicKey());
        } catch (BadPaddingException e) {
            LOGGER.debug("异常:{}", e.getMessage());
            e.printStackTrace();
        }
        return BlankUtil.isEmpty(sign) ? CommonResult.failed(CommonResultCode.ERR_SIGN_MISTAKE) : CommonResult.success(sign);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public synchronized CommonResult<Boolean> balancePay(String sign, String payPassword) throws Exception {
        LOGGER.info("用户余额支付开始---任务执行时间:{},线程名称:{}", LocalDateTime.now(), Thread.currentThread().getName());

        boolean signVerified = false;
        boolean response = true;

        // 验签----再验证密码
        String decrypt = RasUtil.decrypt(sign, RasUtil.getPrivateKey());
        JSONObject result = BlankUtil.isEmpty(decrypt) ? null : JSONObject.parseObject(decrypt);
        if (BlankUtil.isNotEmpty(result) && result.size() == FigureConstant.INT_FOUR) {

            // 此处验签成功,判断支付密码,取值,订单号
            String orderSn = result.getString("sn");
            // 金额
            Long amount = result.getLong("a");
            // 用户id
            Long userId = result.getLong("id");

            Date endTime = result.getDate("t");

            LambdaQueryWrapper<OmsOrder> queryWrapper = Wrappers.<OmsOrder>lambdaQuery()
                                                                .eq(BlankUtil.isNotEmpty(orderSn), OmsOrder::getOrderSn, orderSn);
            if (BlankUtil.isNotEmpty(endTime) && System.currentTimeMillis() > endTime.getTime()) {
                OmsOrder build = OmsOrder.builder().orderSelect(OmsOrder.OrderStatus.FOUR.key()).build();
                boolean update = orderService.update(build, queryWrapper);
                LOGGER.info("订单超时:用户id:{},关闭订单,订单编号:{},修改状态{}", userId, orderSn, update);
                return CommonResult.failed(CommonResultCode.ERR_ORDER_TIMEOUT);
            }
            UmsUser user = UserUtil.getCurrentUser();
            // 判断支付密码
            if (!passwordEncoder.matches(payPassword, user.getPayPassword()) || !user.getUserId().equals(userId)) {
                // 支付密码错误
                LOGGER.info("余额支付:用户id:{},输入支付密码错误", userId);
                return CommonResult.failed(CommonResultCode.ERR_NOT_PAY_CODE);
            }
            UmsIncome umsIncome = incomeService.selectNewestByUserId(Long.valueOf(userId.toString()));
            BigDecimal multiply = DecimalUtil.toBigDecimal(amount);
            if (BlankUtil.isEmpty(umsIncome) || BlankUtil.isEmpty(multiply) || multiply.compareTo(DecimalUtil.toBigDecimal(umsIncome.getBalance())) != NumberUtils.INTEGER_MINUS_ONE) {
                //余额不足
                LOGGER.info("余额支付:用户id:{},余额不足", userId);
                return CommonResult.failed(CommonResultCode.ERR_USER_DEPOSIT);
            }

            OmsOrder order = orderService.getOne(queryWrapper);

            if (BlankUtil.isNotEmpty(order) && multiply.compareTo(DecimalUtil.toBigDecimal(order.getPayAmount())) == NumberUtils.INTEGER_ZERO) {

                Integer orderStatus = order.getOrderStatus();
                Integer orderType = order.getOrderType();
                String transId = order.getTransId();
                if (BlankUtil.isNotEmpty(orderStatus) && orderStatus.equals(NumberUtils.INTEGER_ZERO) && BlankUtil.isNotEmpty(orderType) && BlankUtil.isEmpty(transId)) {
                    StringBuilder sb = new StringBuilder();
                    String balanceId = IdGenerator.INSTANCE.balanceId();
                    Date paymentTime = new Date();
                    BigDecimal dAmount = DecimalUtil.toBigDecimal(amount)
                                                    .divide(new BigDecimal(FigureConstant.INT_ONE_HUNDRED), BigDecimal.ROUND_CEILING, RoundingMode.DOWN);
                    try {
                        sb.append("用户:").append(userId);
                        order.setTransId(balanceId);
                        order.setPaymentTime(paymentTime);
                        order.setPayType(OmsOrder.PayType.THREE.key());
                        switch (EnumTool.getEnum(OmsOrder.OrderType.class, orderType)) {
                            default:
                            case ZERO:
                                //正常订单
                                order.setOrderStatus(NumberUtils.INTEGER_ONE);
                                sb.append("购买普通商品价格:").append(dAmount);
                                break;
                            case ONE:
                                //秒杀订单
                                order.setOrderStatus(NumberUtils.INTEGER_TWO);
                                rabbitTemplate.convertAndSend(RabbitMqSmsConfig.CHANGE_ACTIVE_EXCHANGE, RabbitMqSmsConfig.CHANGE_ACTIVE_ROUTING_KEY, new ChangeActiveDto(order.getCreateTime(), order.getUserId(), orderSn));
                                sb.append("购买秒杀商品价格:").append(dAmount);
                                break;
                            case THREE:
                                order.setOrderStatus(OmsOrder.OrderStatus.SIX.key());
                                response = orderService.updateUserForSeniorPay(order.getUserId(), order);
                                sb.append("购买会员价格:").append(dAmount);
                                break;
                            case FOUR:
                                // 创客订单
                                order.setOrderStatus(OmsOrder.OrderStatus.SIX.key());
                                response = productService.updateByOrderId(order.getOrderId());
                                sb.append("创客商品价格:").append(dAmount);
                                break;
                        }
                    } catch (Exception e) {
                        LOGGER.debug("用户id:{},操作订单,{}异常", userId, orderSn);
                        throw new ApiException(CommonResultCode.FAILED);
                    }

                    //构建对象
                    Long longZero = NumberUtils.LONG_ZERO;
                    UmsIncome build = UmsIncome.builder()
                                               .incomeId(IdWorker.generateId())
                                               .userId(userId)
                                               .income(longZero)
                                               .expenditure(amount)
                                               .balance(umsIncome.getBalance() - amount)
                                               .todayIncome(BlankUtil.isEmpty(umsIncome.getTodayIncome()) ? longZero : umsIncome.getTodayIncome())
                                               .allIncome(BlankUtil.isEmpty(umsIncome.getAllIncome()) ? longZero : umsIncome.getAllIncome())
                                               .incomeType(UmsIncome.IncomeType.NINE.key())
                                               .detailSource("用户余额支付")
                                               .incomeNo(balanceId)
                                               .orderTradeNo(orderSn)
                                               .payType(UmsIncome.PayType.THREE.key())
                                               .createTime(paymentTime)
                                               .remark(sb.toString())
                                               .build();

                    // 这里还会有异常需要再
                    if (response) {
                        try {
                            signVerified = incomeService.save(build) && orderService.updateById(order);
                        } catch (Exception e) {
                            LOGGER.debug("数据库操作异常:{}", e.getMessage());
                            throw new ApiException(CommonResultCode.ERR_DATABASE);
                        }
                    } else {
                        throw new ApiException(CommonResultCode.ERR_PAY_GIFT);
                    }
                } else {
                    return CommonResult.failed(CommonResultCode.ERR_ORDER_STATUS);
                }
            }
        }
        LOGGER.info("用户余额支付结束,{}", signVerified);
        return signVerified ? CommonResult.success(Boolean.TRUE) : CommonResult.failed(Boolean.FALSE);
    }


   /* private void doQuickOrder(OmsOrder order,String tradeNo) {
        LOGGER.info("---处理秒杀分佣逻辑开始---");
        // 要查询出来这个商品是不是第一轮秒杀,查询是不是第一轮秒杀,
        OmsOrderItem orderItem = orderItemService.getOne(new QueryWrapper<OmsOrderItem>().eq("order_id", order.getOrderId()));
        Optional.ofNullable(orderItem).orElseThrow(()-> new ApiException("查询订单错误"));
        QueryWrapper<SmsFlashPromotionProduct> eq = new QueryWrapper<SmsFlashPromotionProduct>().eq("product_id", orderItem.getProductId()).eq("is_publish_status", 1);
        SmsFlashPromotionProduct one = flashPromotionProductService.getOne(eq);
        Optional.ofNullable(one).orElseThrow(()->new ApiException("没有查到次秒杀商品"));
        if(one.getFlashPromotionRound() > 0){
            //轮回秒杀计算收益,先不进入轮回,计算普通的收益
            doRecurrent(order);
        }
        QueryWrapper<UmsUserInvite> queryWrapper = new QueryWrapper<UmsUserInvite>().eq("user_id", order.getUserId());
        UmsUserInvite userInvite = inviteService.getOne(queryWrapper);
        if(!BlankUtil.isEmpty(userInvite) && !BlankUtil.isEmpty(userInvite.getParentId())){
            // 必须达到10元才能计算收益
            if(order.getTotalAmount() < 10){
                return;
            }
            UmsIncome income = incomeService.selectNewestByUserId(order.getUserId());
            UmsIncome build = UmsIncome.builder()
                    .incomeId(IdWorker.generateId())
                    .userId(userInvite.getParentId())
                    .income()
                    .expenditure(0L)
                    .incomeType(1)
                    .detailSource("获得下级收益" + order.getTotalAmount())
                    .orderTradeNo(order.getOrderSn())
                    .orderTradeNo(tradeNo)
                    .build();

        }
        LOGGER.info("---处理秒杀分佣逻辑结束---");*/
    /*}*/

    /*private void doRecurrent(OmsOrder order) {
        // 计算轮回秒杀给客户的收益
        *//*QueryWrapper<UmsUserInvite> eq = new QueryWrapper<UmsUserInvite>().eq("user_id", order.getUserId());
        UmsUserInvite one = inviteService.getOne(eq);
        if(!BlankUtil.isEmpty(one) && !BlankUtil.isEmpty(one.getParentId())){
            // 计算收益,给上级计算
            QueryWrapper<SysTemSetting> qw = new QueryWrapper<SysTemSetting>().eq("tem_type", 0).eq("is_tem_status", 1);
            SysTemSetting temSetting = temSettingService.getOne(qw);
            Optional.ofNullable(temSetting).orElseThrow(()->new ApiException("系统没有设置收益"));
            // 计算能得到的分佣,列如商品是1000元,1000*,他的上级照样有收益是一样的,先不记录下来，

        }*//*
        return ;
    }*/
}
