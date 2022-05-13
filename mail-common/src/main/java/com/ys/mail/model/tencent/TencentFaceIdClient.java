package com.ys.mail.model.tencent;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.hash.Hashing;
import com.ys.mail.domain.FaceIdReq;
import com.ys.mail.exception.ApiAssert;
import com.ys.mail.exception.ApiException;
import com.ys.mail.model.CommonResult;
import org.apache.commons.io.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author ghdhj
 */
@Component
public class TencentFaceIdClient {

    private static final Logger log = LoggerFactory.getLogger(TencentFaceIdClient.class);

    private static final String ACCESS_TOKEN_URL = "https://miniprogram-kyc.tencentcloudapi.com/api/oauth2/access_token";
    private static final String TICKET_URL = "https://miniprogram-kyc.tencentcloudapi.com/api/oauth2/api_ticket";
    private static final String SUCCESS = "0";
    private static final String VERSION = "1.0.0";
    private static final String RANDOM = "ABCDEFGHIJKMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
    private static final String NONCE = "NONCE";



    /**
     * 获取accessToken
     * @return 返回值
     */
    public String getAccessToken(String wbAppId,String secret) throws Exception {
        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put("app_id",wbAppId);
        paramMap.put("secret",secret);
        paramMap.put("grant_type","client_credential");
        paramMap.put("version",VERSION);
        String result = HttpUtil.get(ACCESS_TOKEN_URL, paramMap);
        JSONObject object = JSONObject.parseObject(result);
        String code = object.getString("code");
        if (!SUCCESS.equals(code)){
            throw new ApiException("请求AccessToken失败");
        }
        String accessToken = object.getString("access_token");
        log.info("accessToken:="+accessToken);
        return accessToken;
    }

    /**
     * 获取SignTicket
     * @return 返回值
     */
    public String getSignTicket(String accessToken,String wbAppId) throws Exception {
        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put("app_id",wbAppId);
        paramMap.put("access_token",accessToken);
        paramMap.put("type","SIGN");
        paramMap.put("version",VERSION);
        String result = HttpUtil.get(TICKET_URL, paramMap);
        JSONObject object = JSONObject.parseObject(result);
        String code = object.getString("code");
        if (!SUCCESS.equals(code)){
            throw new ApiException("请求SignTicket失败");
        }
        String signTicket = object.getJSONArray("tickets").getJSONObject(0).getString("value");
        log.info("signTicket:="+signTicket);
        return signTicket;
    }

    /**
     * 获取ApiTicket
     * @return 返回值
     */

    public String getApiTicket(String accessToken,String userId,String wbAppId) throws Exception {
        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put("app_id",wbAppId);
        paramMap.put("access_token",accessToken);
        paramMap.put("type","NONCE");
        paramMap.put("version",VERSION);
        paramMap.put("user_id",userId);
        String result = HttpUtil.get(TICKET_URL, paramMap);
        JSONObject object = JSONObject.parseObject(result);
        String code = object.getString("code");
        if (!SUCCESS.equals(code)){
            throw new ApiException("请求ApiTicket失败");
        }
        String apiTicket = object.getJSONArray("tickets").getJSONObject(0).getString("value");
        log.info("apiTicket:="+apiTicket);
        return apiTicket;
    }
    /**
     * 生成签名
     * @param values 值
     * @param ticket 参数
     * @return 返回值
     */
    public static String sign(List<String> values, String ticket) {
        if (values == null) {
            throw new NullPointerException("values is null");
        }
        values.removeAll(Collections.singleton(null));
        values.add(ticket);
        java.util.Collections.sort(values);
        StringBuilder sb = new StringBuilder();
        for (String s : values) {
            sb.append(s);
        }
        String sign = Hashing.sha1().hashString(sb, Charsets.UTF_8).toString().toUpperCase();
        log.info("sign:="+sign);
        return sign;
    }

    /**
     * 获取签名
     * @param
     * @param ticket
     * @return
     */
    public String getSign(String wbAppId,String userId,String version,String ticket,String nonce){
        List<String> values = new ArrayList<>();
        values.add(wbAppId);
        values.add(userId);
        values.add(version);
        values.add(nonce);
        return sign(values, ticket);
    }



    public String getFaceId(FaceIdReq req){
        String paramStr = JSONObject.toJSONString(req);
        HttpResponse response = HttpUtil.createPost("https://miniprogram-kyc.tencentcloudapi.com/api/server/getfaceid")
                .body(paramStr).contentType("application/json").execute();
        String responseBody = response.body();
        JSONObject object = JSONObject.parseObject(responseBody);
        String code = object.getString("code");
        if (!SUCCESS.equals(code)){
            throw new ApiException("请求faceId失败");
        }
        String faceId = object.getJSONObject("result").getString("faceId");
        log.info("faceId:="+faceId);
        return faceId;
    }

    public CommonResult<Object> getFaceIdResult(String appId, String version, String nonce, String orderNo, String sign, String getFile){
        HashMap<String, Object> map = new HashMap<>();
        map.put("app_id",appId);
        map.put("version",VERSION);
        map.put("nonce",nonce);
        map.put("sign",sign);
        map.put("order_no",orderNo);
        map.put("get_file",getFile);
        String resp = HttpUtil.get("https://miniprogram-kyc.tencentcloudapi.com/api/server/sync", map);
        log.info("身份认证结果{}",resp);
        JSONObject object = JSONObject.parseObject(resp);
        String code = object.getString("code");
        return SUCCESS.equals(code) ? CommonResult.success(Boolean.TRUE) : CommonResult.failed(Boolean.FALSE);
    }



    public static String getRandomNumByLength(int length) {
        String base = RANDOM;
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for ( int i = 0; i < length; i++ ){
            int number = random.nextInt( base.length() );
            sb.append( base.charAt( number ) );
        }
        return sb.toString();
    }

    public String getRandomNumByLength(Integer randomNumLength, String type){
        String rNum = String.valueOf(System.currentTimeMillis());
        Integer typeLength = type.length();
        int i = randomNumLength - rNum.length()-typeLength;
        String nonce = type + rNum + getRandomNumByLength(i);
        log.info("nonce:="+nonce);
        return nonce;
    }

    public static String getVersion(){
        return VERSION;
    }

    public static String getNonce(){
        return NONCE;
    }
}
