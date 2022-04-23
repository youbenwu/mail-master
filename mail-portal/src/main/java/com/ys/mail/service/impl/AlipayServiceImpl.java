package com.ys.mail.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayFundTransUniTransferRequest;
import com.alipay.api.response.AlipayFundTransUniTransferResponse;
import com.ys.mail.constant.AlipayConstant;
import com.ys.mail.controller.FileController;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.alipay.AlipayPaidOutParam;
import com.ys.mail.model.alipay.BusinessParams;
import com.ys.mail.model.alipay.PayeeInfo;
import com.ys.mail.service.AlipayService;
import com.ys.mail.service.CommonPayService;
import com.ys.mail.service.OmsOrderItemService;
import com.ys.mail.service.UmsUserService;
import com.ys.mail.util.AlipayUtil;
import com.ys.mail.util.IdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;

/**
 * @author DT
 * @version 1.0
 * @date 2021-12-04 10:09
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class AlipayServiceImpl implements AlipayService {

    private final static Logger log = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private OmsOrderItemService orderItemService;
    @Autowired
    private UmsUserService userService;
    @Autowired
    private CommonPayService commonPayService;
    @Value("${prop.staticAccessPath}")
    private String accessPath;


    @Override
    public CommonResult<JSONObject> test(String transAmount) throws AlipayApiException {
        // 测试接口可以做为参考
        AlipayPaidOutParam build = AlipayPaidOutParam.builder()
                .outBizNo(IdGenerator.INSTANCE.generateId())
                .transAmount("0.01")
                .productCode(AlipayConstant.PRODUCT_CODE)
                .bizScene(AlipayConstant.BIZ_SCENE)
                .orderTitle(AlipayConstant.USER_DEPOSIT)
                .payeeInfo(PayeeInfo.builder()
                        .identityType(AlipayConstant.IDENTITY_TYPE_LOGON)
                        .identity("13825532901")
                        .name("苏尹姗")
                        .build())
                .remark("吉狐科技用户提现")
                .businessParams(BusinessParams.builder()
                        .payerShowName(AlipayConstant.JH_KJ + AlipayConstant.USER_DEPOSIT)
                        .build())
                .build();

        // 返回响应
        AlipayFundTransUniTransferResponse response = commonPayService.paidOut(build);
        JSONObject result = JSONObject.parseObject(response.getBody());
        return response.isSuccess() ? CommonResult.success(result) : CommonResult.failed(result);
    }

   /* @Override
    public String notifyUrl(HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info("==================支付宝异步返回支付结果开始");

        //1从支付宝回调的request域中取值,获取支付宝返回的参数集合
        Map<String, String[]> aliParams = request.getParameterMap();

        //用以存放转化后的参数集合
        Map<String, String> conversionParams = new HashMap<String, String>(16);
        for (String key : aliParams.keySet()) {
            String[] values = aliParams.get(key);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化;
            // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "uft-8")
            conversionParams.put(key, valueStr);
        }
        LOGGER.info("==================返回参数集合："+conversionParams);
        return notify(conversionParams);
    }


    private String notify(Map<String, String> conversionParams) {
        LOGGER.info("==================支付宝异步请求逻辑处理");

        //签名验证(对支付宝返回的数据验证，确定是支付宝返回的)
        boolean signVerified = false;

        try {
            //调用SDK验证签名
            signVerified = AlipaySignature.rsaCheckV1(conversionParams, "公钥", AlipayConstant.CHARSET, AlipayConstant.SIGN_TYPE);

        } catch (AlipayApiException e) {
            LOGGER.info("==================验签失败 ！");
            e.printStackTrace();
        }

        //对验签进行处理
        if (signVerified) {
            //验签通过
            //获取需要保存的数据,支付宝分配给开发者的应用Id
            String appId=conversionParams.get("app_id");
            //通知时间:yyyy-MM-dd HH:mm:ss
            String notifyTime=conversionParams.get("notify_time");
            //交易创建时间:yyyy-MM-dd HH:mm:ss
            String gmtCreate=conversionParams.get("gmt_create");
            //交易付款时间
            String gmtPayment=conversionParams.get("gmt_payment");
            //交易退款时间
            String gmtRefund=conversionParams.get("gmt_refund");
            //交易结束时间
            String gmtClose=conversionParams.get("gmt_close");
            //支付宝的交易号
            String tradeNo=conversionParams.get("trade_no");
            //获取商户之前传给支付宝的订单号（商户系统的唯一订单号）
            String outTradeNo = conversionParams.get("out_trade_no");
            //商户业务号(商户业务ID，主要是退款通知中返回退款申请的流水号)
            String outBizNo=conversionParams.get("out_biz_no");
            //买家支付宝账号
            String buyerLogonId=conversionParams.get("buyer_logon_id");
            //卖家支付宝用户号
            String sellerId=conversionParams.get("seller_id");
            //卖家支付宝账号
            String sellerEmail=conversionParams.get("seller_email");
            //订单金额:本次交易支付的订单金额，单位为人民币（元）
            String totalAmount=conversionParams.get("total_amount");
            //实收金额:商家在交易中实际收到的款项，单位为元
            String receiptAmount=conversionParams.get("receipt_amount");
            //开票金额:用户在交易中支付的可开发票的金额
            String invoiceAmount=conversionParams.get("invoice_amount");
            //付款金额:用户在交易中支付的金额
            String buyerPayAmount=conversionParams.get("buyer_pay_amount");
            // 获取交易状态
            String tradeStatus = conversionParams.get("trade_status");

            //支付宝官方建议校验的值（out_trade_no、total_amount、sellerId、app_id,查出订单信息

            // 判断订单号的一些业务逻辑.首先查询出来的订单信息不能为null
            String str = "订单";
            if(buyerPayAmount.equals(str)){
                //修改数据库支付宝订单表(因为要保存每次支付宝返回的信息到数据库里，以便以后查证)
                // 判断交易结果
                switch (tradeStatus)
                {
                    // 交易结束并不可退款
                    case "TRADE_FINISHED":
                        //alipaymentOrder.setTradeStatus((byte) 3);
                        break;
                    // 交易支付成功
                    case "TRADE_SUCCESS":
                        //alipaymentOrder.setTradeStatus((byte) 2);
                        break;
                    // 未付款交易超时关闭或支付完成后全额退款
                    case "TRADE_CLOSED":
                        //alipaymentOrder.setTradeStatus((byte) 1);
                        break;
                    // 交易创建并等待买家付款
                    case "WAIT_BUYER_PAY":
                        //alipaymentOrder.setTradeStatus((byte) 0);
                        break;
                    default:
                        break;
                }
                //更新交易表中状态 this.updateByPrimaryKey(alipaymentOrder)
                int returnResult=0;
                //只处理支付成功的订单: 修改交易表状态,支付成功
                if(tradeStatus.equals("TRADE_SUCCESS")) {

                    if(returnResult>0){
                        return "success";
                    }else{
                        return "fail";
                    }
                }else{
                    return "fail";
                }

            }else{
                LOGGER.info("==================支付宝官方建议校验的值（out_trade_no、total_amount、sellerId、app_id）,不一致！返回fail");
                return"fail";
            }

        }else {  //验签不通过
            LOGGER.info("==================验签不通过 ！");
            return "fail";
        }

    }*/
}
