package com.ys.mail.model.oauth;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author ghdhj
 */
public class Auth {
    /**
     * URL
     */
    private final static String URL = "http://identify.accuratead.cn/auth/auth/sdkClientFreeLogin";

    private final static String APP_KEY = "xcjyzx";
    private final static String APP_SECRET = "Q69NpD";
    private static String token = "BearereyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImNyZWF0ZWQiOjE2MzYzNjI5ODE3MTgsImV4cCI6MTYzNjk2Nzc4MX0.dOAAqtvxvNMrFzzr-rYcNWD4fYGgLzfQSbzE33nnQWl-S2ht3utVnGb1V7ddtu0oYOYJNECXZ3MzDVpOBOAqNg";
    private static String opToken = "opToken";
    private static String operator = "CMCC";

    public static void main(String[] args) throws Exception {
        HashMap<String, Object> request = new HashMap<>(16);
        request.put("appkey", APP_KEY);
        request.put("token", token);
        request.put("opToken", opToken);
        request.put("operator", operator);
        request.put("timestamp", System.currentTimeMillis());
        request.put("sign", SignUtil.getSign(request, APP_SECRET));
        String response = postRequestNoSecurity(URL, null, request);

        JSONObject jsonObject = JSONObject.parseObject(response);
        if (200 == jsonObject.getInteger("status")) {
            String res = jsonObject.getString("res");
            byte[] decode = DES.decode(Base64Utils.decode(res.getBytes()), APP_SECRET.getBytes());
            jsonObject.put("res", JSONObject.parseObject(new String(decode)));
        }
        System.out.println(jsonObject);
    }


    public static String postRequestNoSecurity(String url, Map<String, String> headers, Object data) throws Exception {
        String securityReq = JSON.toJSONString(data);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS).build();
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), securityReq);
        Request.Builder builder = new Request.Builder();
        if (!BaseUtils.isEmpty(headers)) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        final Request request = builder.addHeader("Content-Length", String.valueOf(securityReq.length()))
                .url(url)
                .post(body)
                .build();
        Call call = okHttpClient.newCall(request);
        Response response = call.execute();

        String securityRes = response.body().string();
        return securityRes;
    }

}
