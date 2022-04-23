package com.ys.mail.util;

import com.ys.mail.exception.ApiException;
import com.ys.mail.exception.code.CommonResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;

/**
 * 支付宝工具类接口
 *
 * @author DT
 * @version 1.0
 * @date 2021-12-07 19:57
 */

public class AlipayUtil implements Serializable {

    private static final long serialVersionUID = 1L;

    private final static Logger LOGGER = LoggerFactory.getLogger(AlipayUtil.class);


    /**
     * out_biz_no 商户订单号
     * order_id 支付宝转账订单号
     * pay_fund_order_id  支付宝支付资金流水号
     * status  转账单据状态
     * trans_date 订单支付时间，格式为yyyy-MM-dd HH:mm:ss
     */

    /**
     * 支付宝提现工具类接口
     *
     * @param params 提现参数实体
     * @return 返回response
     */
    /*public static AlipayFundTransUniTransferResponse paidOut(AlipayPaidOutParam params) throws AlipayApiException{

        // TODO 申请支付宝用户信息的接口需要审核一个工作日再对接
        CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
        //gateway:支付宝网关（固定）https://openapi.alipay.com/gateway.do
        certAlipayRequest.setServerUrl(AlipayConstant.SERVER_URL);
        //APPID 即创建应用后生成,详情见创建应用并获取 APPID
        certAlipayRequest.setAppId(AlipayConstant.APP_ID);
        //开发者应用私钥，由开发者自己生成
        certAlipayRequest.setPrivateKey(AlipayConstant.APP_PRIVATE_KEY);
        //参数返回格式，只支持 json 格式
        certAlipayRequest.setFormat(AlipayConstant.FORMAT);
        //请求和签名使用的字符编码格式，支持 GBK和 UTF-8
        certAlipayRequest.setCharset(AlipayConstant.CHARSET);
        //商户生成签名字符串所使用的签名算法类型，目前支持 RSA2 和 RSA，推荐商家使用 RSA2。
        certAlipayRequest.setSignType(AlipayConstant.SIGN_TYPE);
        certAlipayRequest.setCertPath(solutePath(AlipayConstant.APP_CERT_PATH));
        //支付宝公钥证书文件路径（alipay_cert_path 文件绝对路径）
        certAlipayRequest.setAlipayPublicCertPath(solutePath(AlipayConstant.ALIPAY_CERT_PATH));
        //支付宝CA根证书文件路径（alipay_root_cert_path 文件绝对路径）
        certAlipayRequest.setRootCertPath(solutePath(AlipayConstant.ALIPAY_ROOT_CERT_PATH));

        // 传入参数进支付宝,返回response,后端根据response中的数据进行做判断
        DefaultAlipayClient alipayClient = new DefaultAlipayClient(certAlipayRequest);
        AlipayFundTransUniTransferRequest request = new AlipayFundTransUniTransferRequest();
        request.setBizContent(params.getJsonString());
        AlipayFundTransUniTransferResponse response = alipayClient.certificateExecute(request);
        LOGGER.info("{}",response.isSuccess());
        return response;
    }*/
    public static String solutePath(String path) {
        Resource resource = new ClassPathResource(path);
        String key = null;
        try {
            key = resource.getFile().getPath();
        } catch (IOException e) {
            e.printStackTrace();
            throw new ApiException(CommonResultCode.ERR_READ_FILE);
        }
        return key;
    }

    public static String alipayKey(String key) {
        // java8自带关闭流功能
        Resource resource = new ClassPathResource(key);
        String data = null;
        StringBuilder sb = new StringBuilder();
        try (InputStream is = resource.getInputStream(); InputStreamReader isr = new InputStreamReader(is); BufferedReader br = new BufferedReader(isr)) {
            while (!BlankUtil.isEmpty((data = br.readLine()))) {
                sb.append(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ApiException(CommonResultCode.ERR_READ_FILE);
        }
        return sb.toString();
    }

}
