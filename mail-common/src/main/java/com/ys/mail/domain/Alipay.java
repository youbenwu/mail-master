package com.ys.mail.domain;

import java.io.Serializable;

/**
 * 用一句简单的话来描述下该类
 * 阿里支付,底下会有很多种支付,比如A公司,B公司等
 * @author DT
 * @date 2022-05-30 15:15
 * @since 1.0
 */
public class Alipay implements Serializable {

    private static final long serialVersionUID = 1L;

    private String serverUrl;

    private String appId;

    private String privateKey;

    private String format;

    private String charset;

    private String signType;

    private String appCertPath;

    private String alipayCertPath;

    private String alipayRootCertPath;

    private String identityTypeLogon;

    private String productCode;

    private String bizScene;

    /**
     * 先写个模型,一步一步的改
     */


}
