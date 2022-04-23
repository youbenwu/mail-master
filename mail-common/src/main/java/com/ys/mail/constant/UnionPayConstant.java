package com.ys.mail.constant;

import java.io.Serializable;

/**
 * 云闪付一些必用的常量
 * @author DT
 * @version 1.0
 * @date 2021-12-08 14:15
 */

public class UnionPayConstant implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 上线时分配->云闪付
     */
    public static final String JUH_ACCOUNT = "";

    /**
     * 上线时分配->云闪付
     */
    public static final String JIH_ACCOUNT = "";

    /**
     * 上线时分配->云闪付系统号
     */
    public static final String JUH_SYS_CODE = "";

    /**
     * 上线时分配->云闪付系统号
     */
    public static final String JIH_SYS_CODE = "";

    /**
     * 交易请求地址
     */
    public static final String URL = "http://117.48.192.183:8880/cgi-bin/n_web_pay.api";

    /**
     * sign_key xxx科技
     */
    public static final String JUH_SIGN_KEY = "";
    /**
     * sign_key yyy科技
     */
    public static final String JIU_SIGN_KEY = "";

    /**
     * 云闪付交易成功状态码
     */
    public static final String CODE = "0000";

    /**
     * 云闪付交易状态码名称
     */
    public static final String ERROR_CODE = "errorcode";

    /**
     * 接收统一支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数,本地测试可以弄个内网穿透
     */
    public static final String NOTIFY_URL = "http://daweihu.natapp1.cc/unionPay/notifyUrl";

    /**
     * 前端页面跳转地址，支付完成后将回跳该地址，不能携带参数
     */
    public static final String CALLBACK_URL = "www.abc.com/callbackurl.html";

    /**
     * 公钥
     */
    public static final String PUB_KEY = "";

    /**
     * 编码格式
     */
    public static final String CHARSET = "UTF8";

    /**
     * 银联APP支付
     */
    public static final String PAY_MODE_API_CU_APP = "API_CUAPP";

    /**
     * 微信APP支付
     */
    public static final String PAY_MODE_API_WX_APP = "API_WXAPP";

    /**
     * 支付宝APP支付
     */
    public static final String PAY_MODE_API_ZFB_APP = "API_ZFBAPP";

    /**
     * 云闪付交易成功
     */
    public static final String RESULT_SUCCESS = "1";

    /**
     * 云闪付交易失败
     */
    public static final String RESULT_ERROR = "2";

    /**
     * 云闪付待支付
     */
    public static final String RESULT_UNPAID = "3";

    /**
     * 云闪付支付已失效
     */
    public static final String RESULT_LOST = "4";

    /**
     * 云闪付已退款或部分退款
     */
    public static final String RESULT_REFUND = "5";

    /**
     * 吉狐科技用户购买商品
     */
    public static final String JIU_USER_BUY_PRODUCT = "xxx科技用户购买商品";

    /**
     * 桔狐科技用户购买商品
     */
    public static final String JUH_USER_BUY_PRODUCT = "yyy科技用户购买商品";

    /**
     * 云闪付支付成功
     */
    public static final String SUCCESS = "SUCCESS";

    /**
     * xxx科技公钥
     */
    public static final String JIU_PUB_KEY = "";

    /**
     * yyy科技公钥
     */
    public static final String JUH_PUB_KEY = "";

    /**
     * 错误消息
     */
    public static final String ERROR = "ERROR";
}
