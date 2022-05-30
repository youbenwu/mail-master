package com.ys.mail.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * 用一句简单的话来描述下该类
 *
 * @author DT
 * @date 2022-05-30 15:01
 * @since 1.0
 */
public abstract class Pay {

    protected Logger logger = LoggerFactory.getLogger(Pay.class);

    protected IPayMode payMode;

    public Pay(IPayMode payMode) {
        this.payMode = payMode;
    }

    /**
     * 桥接那种支付模型,比如支付宝,微信,云闪付,第三方支付等,人脸,app,web,指纹,等等
     * @param uId 用户ID
     * @param tradeId 订单Id
     * @param amount 金额
     * @return 返回值
     */
    public abstract String transfer(String uId, String tradeId, BigDecimal amount);
}
