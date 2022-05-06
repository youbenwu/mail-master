package com.ys.mail.component;

import com.shuyuanwl.sms.api.bean.DownRes;
import com.shuyuanwl.sms.api.core.ApiSender;
import com.ys.mail.constant.FigureConstant;
import com.ys.mail.exception.ApiAssert;
import com.ys.mail.exception.code.BusinessErrorCode;
import com.ys.mail.util.BlankUtil;
import org.springframework.stereotype.Component;

/**
 * @author DT
 * @version 1.0
 * @date 2021-11-09 11:08
 */
@Component
public class SmsClient {

    /**
     * url地址
     */
    private static final String URL = "http://api.shuyuanwl.com:8080/api/sms/send";

    /**
     * 短信内容
     */
    private static final String CONTENT = "您的短信验证码为：";

    /**
     * 轻创营登录账号
     */
    private static final String JIH_ACCOUNT = "qcy@fhkjgd";

    /**
     * 卖乐吧登录账号
     */
    private static final String JUH_ACCOUNT = "mlb@fhkjgd";

    /**
     * 扩展码
     */
    private static final String EXT_NO = "01";

    /**
     * 密码
     */
    private static final String PASSWORD = "134314";

    /**
     * 批次号
     */
    private static final String BATCH_NO = "";


    public void sendRegisterVerify(String phone, String verifyCode, Byte type) {
        String key = type == 0 ? JIH_ACCOUNT : JUH_ACCOUNT;
        DownRes send = ApiSender.send(URL, key, PASSWORD, phone, CONTENT + verifyCode, EXT_NO, BATCH_NO);
        ApiAssert.noEq(BlankUtil.isEmpty(send) ? null : send.getCode(), FigureConstant.SUCCESS, BusinessErrorCode.ERR_SMS_MISTAKE);
    }
}
