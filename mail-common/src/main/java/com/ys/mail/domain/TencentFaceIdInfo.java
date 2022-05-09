package com.ys.mail.domain;

import com.alibaba.fastjson.JSONObject;
import com.ys.mail.constant.WarningsConstant;
import com.ys.mail.model.tencent.TencentFaceIdClient;
import com.ys.mail.util.BlankUtil;
import lombok.Data;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author ghdhj
 */


public class TencentFaceIdInfo extends FaceProvider{

    private static final Logger log = LoggerFactory.getLogger(TencentFaceIdInfo.class);

    /**
     * 腾讯人脸轻创营核身sdkwbappid
     */

    private static final String QCY_WB_APPID = "IDAdiCgb";


    /**
     * 腾讯轻创营人脸核身sdk Secret
     */
    private static final String QCY_SECRET = "avRuwyZTSrKSWDJthIkBFI9ebgdcSczDvEypeg0eaI3bmWQTVpGrWiJF6Qk6PJxf";

    /**
     * 轻创营-licence
     */
    private static final String QCY_LICENCE = "TpUe/LHIiY7lbeabDvenK+MwJRzCgZEJ2vzsB154A+yWKxhWN3cWhGCbeKseZt+NdehlI2HdqEsbPEnmZVe8BTdhB7MgtHVaf/L7ykKLtLSKw+7drY4b4lKqdfD0nYOP0n3G8694f03LaYZ9kDelF2R78J4x3HzrkkX3tRcSisKeWPxctR9c4L0HxENTCsRS0ZkcfLw5hNIX1cWovk2wpVLlKWNK4huQCwcZ0dgxHl2ymTP89HmNcqedy5S3wuao+fuSKFOvic3EplXHfrsbYNCySsqeYHuKJlRX1goFREJM8qJrYYUWj2zJWrAp5KmLbUcjT+y8CJCXCLZGFHjqwA==";

    /**
     * 腾讯人脸轻创营核身sdkwbappid
     */
    private static final String MLB_WB_APPID = "IDA8JT8c";


    /**
     * 腾讯轻创营人脸核身sdk Secret
     */
    private static final String MLB_SECRET = "ftTzy9d3WykmUxZbFyfgBHT8P0YvhM9RXRDpGQXXpJQpnqpke0X2lde8728jsaV4";

    /**
     * 卖乐吧-licence
     */
    private static final String MLB_LICENCE = "ACR5RZUtVMkfmiUuKGiSyV9OPW2beywNWhmW0Sbmy3EeXw7ZJ2ePzOSyDIlUSyYLhUmttcs16ol6Yari8acu1Fg9lIvN6J8KrIoknKdHni/5MkI9I3HIeuv/Zn/nvXHaKF3gTY0J4sIVnplYEFRQ6nrEkN0XXV3LT4umxBrf0j13zLLKfcuYoBcu0jQQI2tACs9blxqPNyQArwENas4out4bvUSBEXfaBuz1qS8JFOgtRAmZzJKQlub2gGWcccmSQ7XO9MlpMq2jE+wBF9Gs5BfP2mTPNUQeFq4wSArV51omhRY0fZ3K6+vTm0YL7aq1nEtbxsqoZE16k+VnP9gkFQ==";



    /**
     * 传空默认就是轻创营,0->轻创营,1->卖乐吧,默认不让传空,上层做改变
     * @param obj 参数
     * @return 返回值
     * @param <T> 参数
     */
    @SuppressWarnings(WarningsConstant.UNCHECKED)
    @Override
    public <T> T authorizationCode(Object obj) {
        JSONObject result = new JSONObject();
        if(obj.equals(NumberUtils.INTEGER_ZERO)){
            result.put("wbAppId",QCY_WB_APPID);
            result.put("secret",QCY_SECRET);
            result.put("license",QCY_LICENCE);
        }else{
            result.put("wbAppId",MLB_WB_APPID);
            result.put("secret",MLB_SECRET);
            result.put("license",MLB_LICENCE);
        }
        log.info("result:{}",result);
        return (T)result;
    }
}
