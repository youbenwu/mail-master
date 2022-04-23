package com.ys.mail.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayFundTransUniTransferRequest;
import com.alipay.api.response.AlipayFundTransUniTransferResponse;
import com.ys.mail.constant.AlipayConstant;
import com.ys.mail.model.alipay.AlipayPaidOutParam;
import com.ys.mail.service.CommonPayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * @Desc 通用支付/提现服务
 * @Author CRH
 * @Create 2022-02-19 09:48
 */
@Service
public class CommonPayServiceImpl implements CommonPayService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonPayServiceImpl.class);

    @Value("${prop.staticAccessPath}")
    private String accessPath;

    @Override
    public AlipayFundTransUniTransferResponse paidOut(AlipayPaidOutParam params) throws AlipayApiException {
        CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
        //gateway:支付宝网关（固定）https://openapi.alipay.com/gateway.do
        certAlipayRequest.setServerUrl(AlipayConstant.SERVER_URL);
        //AppID 即创建应用后生成,详情见创建应用并获取 AppID,吉狐 AlipayConstant.APP_ID
        // AppID,吉虎
        certAlipayRequest.setAppId(AlipayConstant.APP_ID);
        //开发者应用私钥，由开发者自己生成,吉狐 AlipayConstant.APP_PRIVATE_KEY
        // 私钥,吉虎
        certAlipayRequest.setPrivateKey(AlipayConstant.APP_PRIVATE_KEY);
        //参数返回格式，只支持 json 格式
        certAlipayRequest.setFormat(AlipayConstant.FORMAT);
        //请求和签名使用的字符编码格式，支持 GBK和 UTF-8
        certAlipayRequest.setCharset(AlipayConstant.CHARSET);
        //商户生成签名字符串所使用的签名算法类型，目前支持 RSA2 和 RSA，推荐商家使用 RSA2。
        certAlipayRequest.setSignType(AlipayConstant.SIGN_TYPE);
        // new File(accessPath+AlipayConstant.APP_CERT_PATH).getPath(),吉狐 AlipayConstant.APP_CERT_PATH
        certAlipayRequest.setCertPath(new File(accessPath + AlipayConstant.APP_CERT_PATH).getPath());
        //支付宝公钥证书文件路径（alipay_cert_path 文件绝对路径） AlipayConstant.ALIPAY_CERT_PATH
        certAlipayRequest.setAlipayPublicCertPath(new File(accessPath + AlipayConstant.ALIPAY_CERT_PATH).getPath());
        //支付宝CA根证书文件路径（alipay_root_cert_path 文件绝对路径）AlipayConstant.ALIPAY_ROOT_CERT_PATH
        certAlipayRequest.setRootCertPath(new File(accessPath + AlipayConstant.ALIPAY_ROOT_CERT_PATH).getPath());

        // 传入参数进支付宝,返回response,后端根据response中的数据进行做判断
        DefaultAlipayClient alipayClient = new DefaultAlipayClient(certAlipayRequest);
        AlipayFundTransUniTransferRequest request = new AlipayFundTransUniTransferRequest();
        request.setBizContent(params.getJsonString());
        AlipayFundTransUniTransferResponse response = alipayClient.certificateExecute(request);
        LOGGER.info("{}", response.isSuccess());
        return response;
    }
}
