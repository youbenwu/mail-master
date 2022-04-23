package com.ys.mail.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.response.AlipayFundTransUniTransferResponse;
import com.ys.mail.model.alipay.AlipayPaidOutParam;

/**
 * @Desc 通用支付/提现接口
 * @Author CRH
 * @Create 2022-02-19 09:47
 */
public interface CommonPayService {

    // 提现接口
    AlipayFundTransUniTransferResponse paidOut(AlipayPaidOutParam params) throws AlipayApiException;

}
