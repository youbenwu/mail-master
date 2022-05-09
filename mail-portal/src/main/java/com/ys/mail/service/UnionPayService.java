package com.ys.mail.service;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.param.AliBuyProductParam;
import com.ys.mail.model.param.BalanceProductParam;
import com.ys.mail.model.param.BuyProductParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author DT
 * @version 1.0
 * @date 2021-12-08 14:53
 */

public interface UnionPayService {

    /**
     * 云闪付回调地址
     *
     * @param request  请求参数
     * @param response 返回值
     * @return 返回值
     */
    String notifyUrl(HttpServletRequest request, HttpServletResponse response);

    /**
     * 云闪付普通商品立即购买接口
     *
     * @param param 实体对象
     * @return 返回值
     */
    CommonResult<JSONObject> buyProduct(BuyProductParam param);

    /**
     * 支付宝单笔支付接口
     *
     * @param param 实体参数
     * @return 返回值
     * @throws AlipayApiException 阿里支付异常向外抛出
     */
    CommonResult<String> aliPayBuyProduct(AliBuyProductParam param) throws AlipayApiException;

    /**
     * 支付宝成功后的回调地址
     *
     * @param request  请求参数
     * @param response 响应参数
     * @return 返回值
     */
    String aliPayNotify(HttpServletRequest request, HttpServletResponse response);

    /**
     * 余额支付购买接口
     *
     * @param param 实体对象
     * @return 返回值
     */
    CommonResult<Boolean> balancePaymentBuyProduct(BalanceProductParam param) throws AlipayApiException;

    /**
     * 余额支付生成签名
     *
     * @param param 传参实体
     * @return 返回值
     */
    CommonResult<String> balancePaySign(AliBuyProductParam param) throws Exception;

    /**
     * 余额支付
     *
     * @param sign        签名
     * @param payPassword 支付密码
     * @return 返回值
     * @throws Exception 抛给java虚拟机异常
     */
    CommonResult<Boolean> balancePay(String sign, String payPassword) throws Exception;
}
