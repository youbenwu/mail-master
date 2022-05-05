package com.ys.mail.constant;

import java.io.Serializable;

/**
 * 支付宝提现一些常量
 * @author DT
 * @version 1.0
 * @date 2021-12-03 17:56
 */

public class AlipayConstant implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * gateway:支付宝网关（固定）https://openapi.alipay.com/gateway.do
     */
    public static final String SERVER_URL = "https://openapi.alipay.com/gateway.do";

    /**
     * app_id 鸿盛源科技提现用
     */
    public static final String APP_ID = "2021003124698698";

    /**
     * 鸿盛源科技私钥，转账使用
     */
    public static final String APP_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCgd7ecJlKrFb2TLCm3TyGs6rl6hnlz0hIJYzt+Bw3k61unDnlnEwwAmJuegmHHqXvM+Fc2PLqHXB9K5xZlRI0TkD9n2qbJoVLmsQcq50CIef8k8N+74A4RBvt+EX+3+ZTjHfEwPJp4YIlrsMcSjw3jxWXH8T5XVyS9+LgnWZQDJNdhY12/urTX1xIaSRDxCT6lzDBvKrbtH05IQg6bGgNrn7WLjHB6ihSs6xEDF1qAERWkdcaneoUQM4vp3haiFofQN7tRVL8yr2/mALhLcDJjLb+QkOF5zSpsVVU0JOkl4JmRCTefAGRywoiP9a4I02PHeGfhPvZ8tcAeCirYmmSDAgMBAAECggEAMOTphGHE9K5WKj7doyYwa6DFzUimPx02M7gRy4/YsvTn41ossbSDBrd5S24M3yqMN2NAJ52PdGkke8037Llbhyd/umRMR8mpGfO87Fy0NCGAFcBzW6qkfpmzDbHFSch5jk50TC9bHezEUpT1w3jlUY63OZoPVcuOvXfInOzVhbM6boqVBfV67c0/XD8yX0WPSLGSA7pozjiO5UUwnT/7qJsidl+5cd5GUPGOoxeJeSE658d2z1rq9cmTDekXHllyYJQqh2REadioJvw8oOS11Qhwj4GyWKXc1oVv321jAxlUNd12IDmRkbdDGhz9Dkckwo60ng4C8htm5Ey/+9duoQKBgQDn4lNTYfKzTpUVFC1JtoKubDFnGa8WdIZ+M4bUXoHWPrZWE5Ls2Rkc973/VoHqbISNMq+ID6fZ9tCIIrsIvbpUmp8tyAhudF9nbz8SViJ34bnEstDLg71nknS01uc9qIGmR/xZNGxf5Yn5YXYwG0QL5qvwyhcwI8f/hF0zW09I0QKBgQCxKACzYGcj93sSvACkuW4viMgyvC1ufbun3gPagU0hTzuprSYrCahY2plQBY0pZ5mOud5dK7doxd8MjaNRE0lVKzsBQQ+NPSoSzSG5r7NAqz4KBR3qkwZ/u/CEnGaszc+r5t35Tbl+LtSUV93YD6HxxYztfCr60U2HlNJOsgdtEwKBgQCrqBStHk4vRJYCQ6x+6/fzrS4pN48wnVROqlA+eDO/aS9LqzPR16jKk+93zQ9hfbHmYMYmgAhux1J55NP2VZVROiFqH7F2KvnRNT5IkbD0dAo0PXdpoP2eZYG1uXz3XP2VHacGVlN/7glkrixy0kxeTwWru77MqUBNvCvOb+LScQKBgA2gZxVlF+njUlspjN8eyEPtwIBuiwwRrcqGwEMhaP5j6tHtWJ882e8juWE3XBlQN7aLw3flMfFkLvj3OBW3Zw+fCKXbDEgv6TmS/8IZqq8RvO7mX9u4PWq72Q9KPHeUSSIO2wofGWKEVaOnco/4+9rt+B4YS/IJAW6pWtfkx79zAoGAYbYJPTvei8LGLqDSRpieBX7miSzmIKslqjgfK5UtH4woJtxae1HmoPZNp1Qi5xXt1E9bZziIu+a7t/QaDNg3OW71jX+n9SO6OhqHfY7yI4a16P7hlh3yUmSZttcUpn12NS6VpbWKYeYKGvJTlTgxbQHN7kpoTtNWD27tJgsjQIE=";

    /**
     * 参数返回格式，只支持 json 格式
     */
    public static final String FORMAT = "json";

    /**
     * 请求和签名使用的字符编码格式，支持 GBK和 UTF-8
     */
    public static final String CHARSET = "UTF-8";

    /**
     * 商户生成签名字符串所使用的签名算法类型，目前支持 RSA2 和 RSA，推荐商家使用 RSA2。
     */
    public static final String SIGN_TYPE = "RSA2";

    /**
     * 应用公钥证书路径
     */
    public static final String APP_CERT_PATH = "crt/hsy_tx_appCertPublicKey_2021003124698698.crt";

    /**
     * 支付宝公钥证书文件路径
     */
    public static final String ALIPAY_CERT_PATH = "crt/hsy_tx_alipayCertPublicKey_RSA2.crt";

    /**
     * 支付宝CA根证书文件路径
     */
    public static final String ALIPAY_ROOT_CERT_PATH = "crt/hsy_tx_alipayRootCert.crt";

    /**
     * 支付宝登录用户id
     */
    public static final String IDENTITY_TYPE_LOGON = "ALIPAY_LOGON_ID";

    /**
     * 支付宝会员的用户 ID
     */
    public static final String IDENTITY_TYPE_USER = "ALIPAY_USER_ID";

    /**
     * 销售产品码。单笔无密转账固定为 TRANS_ACCOUNT_NO_PWD。
     */
    public static final String PRODUCT_CODE = "TRANS_ACCOUNT_NO_PWD";

    /**
     * 业务场景。单笔无密转账固定为 DIRECT_TRANSFER。
     */
    public static final String BIZ_SCENE = "DIRECT_TRANSFER";

    /**
     * 用户提现
     */
    public static final String USER_DEPOSIT = "用户提现";

    /**
     * 鸿盛源科技
     */
    public static final String JH_KJ = "鸿盛源科技";

    /**
     * 鸿盛源科技应用私钥,鸿盛源
     */
    public static final String JIU_APP_PRIVATE_KEY = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCNYg8iPkA1tqBqeifnte+6dACgEm3gzGgKdRmfPzH5qZhTMXW7uc4nNy0ENfCdMVTg+Q+m8Q8B30qV8u72o5HH9mMH5SebwbggYBkeygjbsmNIYWSpIvUoXuOWJNZ3hfni01Bfz1ooNOxmZa/PZdSUXXnBbF7bA+YN4eZw10pHviY2jznSWgMD7cPLlCzmooOiSrabHqyez9MSbcTX2gvX3TCRNVAKsvrqOoMwtJ4c0tiYdF7oTA5vwTL7QEGIVk0ucC3H4FKAWsSUMIhxZ6/NG+8iLXBNbY5/U49TjaoLyBfpiX8s3kAxvar47lUmHW50+cQuRMxhtF2pfd9ISJVjAgMBAAECggEAMY0RksXQiULFfCvt+na4W1e2tGNhIS1itW+PGFH0uPSoj1fBVJbtWq8q1tP0zZLxlZuU0cSgpt5UbLNk7woq2LNQx/6XYC6H0sj7ZOAYNK+TvlVsCx9WXtzK1N96Tw+kocu/2qR4i6joxYaUFJMy6wDL4G+T2xzFr03iDuBVh0jmCcOZAGvUKyJfL9kN/rpKEVgSuB7nFVixWjL0s0djqWrAHJ+bbW8WXdPM9kGk5trUXxlG2bSsQa3vf7wHvGaGOGjuKPXLxqIsm87BbnlpZldZEInkh/uZ4iKgUgmVDP0//2fcPOMRJU28bKvlgWxTWHldv1pVEOQUsOvjmg1dEQKBgQDXRIzIdDEOv28/Q4sCRm++/CRpQfNdEkRpidvJT56seg1O16aRXTadJDnFc6pFn5Z8+bKatIWRsnvW8M7DvSI3sDFAGqMRuhPCOXUI7SUKL4dchVGLyyY2BjrDO9ldcQP6pwP2ZqjFQZAdWyQq1Ssb8u2kauLu+EYHdMHa9yZaKwKBgQCoIpMJFF4n8DTMTkFL+bRNjYFkrHDsSOfBdhC61IWGHxHVrCQ/2mKWzkKDSoDA09ERtyWnDpxNWE1Wcw9RI+hBJpAVUoQHlRfRDuq9tKqzy5pO3zVViU9cVRGubVjZiyuT5Vu7TKc1RmOttfMRMNag37pnMpRh8KKJG3YSwxitqQKBgQCxNiZrUnOcP23Wd8T0z4VohOt4lyk/wiz9cNa1R81JEYTpFdBytHikLlE8wfY3MKRTJ7+nadrXUUZvcHfWXVnQ3EP/EK6ThkqDxocZHRgz5WKagMnbuhYvvTscw6UyLjpnWpyBeIuRL2LzN7dzZ18AuCB4DOHadho4Qx7fIjy5VwKBgQCZQ/Meurdd1sR6XPd+51/ptg12lQ7XBVpl61cyyBnzeg0K50GgB1YJvHm7LsYRgMS7TmV/VH08sDrHjFkxEPddtGZxWWfLdahhRSTMy400GmxOa4A1udZjB5T3mOv+WrPdvJ0TLrvZf5HLvumVCR0NRwSnn97NLMstCqBmV/v+CQKBgQDDBpKG7enDPkkUvmG6t70Y7scYbJ11OusBN76URXUi2J5zIVD3GRVcAxi8VHvoLDxTMxEK9lX2FHpEq0T/0RAUeOAkbNU0uINTqTB29lRhWxlFAKW7N3fjEmKmtZeXcczmPFASQPMdeLpPybrngxVjDgjLH+7C7RFRfb4SlwU4YQ==";

    /**
     * 吉狐科技应用公钥,大尾狐
     */
    public static final String JIU_APP_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAusjfgjjnZhbQz25Z1ylM5TuF0Ts7fK1XEaHGROQqeccekJf2kx4C/cqWUaHdKda0SVj51m83tNWTaZEybe053ywltLh1CA7Jf+Q5CzkSwriYvG7PiBff/sInOkxwH4BTDDafIAgYECR6UDeMkh0BY7b3iUa43UpeeVAMcAbNZ5EmHSGH9nWWy3kosPF/nyIfBHP28cneAU0yRDQg+D4ql8qphYmtcJo5eMOPMGljGtWZ3xNLeXxvv6EnUVa4vInDIvj38VaB+FBJZ7IOBR3hSKDWVoETB4ktnansz68XZPP8gudgzbHV3+Epss8eqsXRV9uhYSv43fLVi6xhW7A1PwIDAQAB";

    /**
     * 鸿盛源科技APP_ID
     */
    public static final String JIU_APP_ID = "2021003124633123";

    /**
     * 吉狐科技应用公钥证书路径
     */
    public static final String JIU_APP_CERT_PATH = "crt/hsy_appCertPublicKey_2021003124633123.crt";

    /**
     * 吉狐科技支付宝公钥证书文件路径
     */
    public static final String JIU_ALIPAY_CERT_PATH = "crt/hsy_alipayCertPublicKey_RSA2.crt";

    /**
     * 吉狐科技支付宝CA根证书文件路径
     */
    public static final String JIU_ALIPAY_ROOT_CERT_PATH = "crt/hsy_alipayRootCert.crt";

    /**
     * 支付宝支付成功后的回调地址,自由切换,可以配置生产和测试自由切换
     */
//    public static final String NOTIFY_URL = "http://8.134.35.199:8019/unionPay/aliPayNotify";
    /*public static final String NOTIFY_URL = "http://yyy.yyy.yyy:8019/unionPay/aliPayNotify";*/
    public static final String NOTIFY_URL = " http://daweihu.natapp1.cc/unionPay/aliPayNotify";

    /**
     * 销售产品码，商家和支付宝签约的产品码
     */
    public static final String PRODUCT_CODE_PAY = "QUICK_MSECURITY_PAY ";

    /**
     * 交易成功
     */
    public static final String TRADE_SUCCESS = "TRADE_SUCCESS";

    /**
     * 交易关闭
     */
    public static final String TRADE_CLOSED = "TRADE_CLOSED";

    /**
     * 交易完成
     */
    public static final String TRADE_FINISHED = "TRADE_FINISHED";

    /**
     * 支付宝回调成功
     */
    public static final String SUCCESS = "success";

    /**
     * 支付宝回调失败
     */
    public static final String FAIL = "fail";


    /**
     * ccc科技-APP_ID
     */
    public static final String JH_YF_APP_ID = "";

    /**
     * ccc科技-应用公钥证书路径
     */
    public static final String JH_YF_APP_CERT_PATH = "";

    /**
     * ccc科技-支付宝公钥证书文件路径
     */
    public static final String JH_YF_ALIPAY_CERT_PATH = "";

    /**
     * ccc科技-支付宝CA根证书文件路径
     */
    public static final String JH_YF_ALIPAY_ROOT_CERT_PATH = "";

    /**
     * 吉狐科技购买描述语
     */
    public static final String JIU_SUBJECT = "鸿盛源科技用户购买商品";

    /**
     * ccc科技购买描述语
     */
    public static final String JH_YF_SUBJECT = "ccc科技用户购买商品";

    /**
     * ccc科技应用私钥,呼啦兔
     */
    public static final String JH_YF_APP_PRIVATE_KEY = "";


    /**
     *订单相对超时时间。从交易创建时间开始计算。
     * 该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）。 该参数数值不接受小数点， 如 1.5h，可转换为 90m。
     * 当面付场景默认值为3h。设置为1m-5m之内,默认都是5m
     */
    public static final String TIMEOUT_EXPRESS = "30m";
}
