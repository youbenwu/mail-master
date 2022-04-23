package com.ys.mail.service;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.param.AliBuyProductParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author DT
 * @version 1.0
 * @date 2021-12-04 10:09
 */

public interface AlipayService {
    /**
     * 支付宝单笔提现测试
     * @param transAmount 金额
     * @return 返回值
     */
    CommonResult<JSONObject> test(String transAmount) throws AlipayApiException;

    /**
     * 支付宝支付成功回调地址
     * @param request 请求
     * @param response 响应
     * @return 返回值
     */
   /* String notifyUrl(HttpServletRequest request, HttpServletResponse response);*/

}
