package com.ys.mail.domain;

/**
 * 用一句简单的话来描述下该类
 * 支付抽象验证类
 * @author DT
 * @date 2022-05-30 15:02
 * @since 1.0
 */
public interface IPayMode {

    /**
     * 安全验证,比如风控的密码支付,指纹支付都会走这一步
     * @param uId 用户Id
     * @return 返回值
     */
    boolean security(String uId);
}
