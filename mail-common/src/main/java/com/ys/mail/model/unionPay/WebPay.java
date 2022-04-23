package com.ys.mail.model.unionPay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.mail.util.StringUtil;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author ghdhj
 */
public class WebPay {

    public static void main(String[] str) throws Exception {
        //系统号上线时分配 测试用20000023
        String syscode = "20000023";
        //商户号 系统对接时提供，唯一。
        String account = "9205000002";
        //商户流水号
        String app_id = "2021091410200100";
        String trans_time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String amount = "100"; //交易金额
        //交易请求地址
        String url = "http://117.48.192.183:8843/cgi-bin/n_web_pay.api";
        String callback_url = "www.abc.com/callbackurl.html";
        String notify_url = "https://test6.jufucloud.com/cgi-bin/success_no_notify.cgi";
        String signkey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCpFj9Cns2M6ro+\n" +
                "QSRKPPZtYwf17pRAguTUYREocnhwqLS9/W0Mr0WnTYG5JisfWfvP3QkFZOZrHmHA\n" +
                "O7x+YfgnqHCjDAReVgANz5phiGwztTzxu2+SInJG9/tPdqk6z6vu5atOO0rXaYDD\n" +
                "9u4LSpCgzbubCz3kOvydchgzyT1UhFfpNTnsJPTKkezDwixwBRLJ/zB/UM9vUV5F\n" +
                "b0g0NVN5nEV2b7u0LmvyyDL2Z/yRGBElBgWZTf1Dsph4TSiY2452aaVeRhnEq5ZD\n" +
                "i/Cm0fFcNNJCcl7Rpwn+DRuZiHxxjMeYO01dX4U4b+ISIg6+vGFmuO3cCgBenGlA\n" +
                "thKRwE6JAgMBAAECggEBAKO7FDTJm5c58HC6GY79q05PF/VQOiSh2z3AZKH6/p9w\n" +
                "7dwPm9otmtAfoF8OE7G/K7Sjf8nGY80UVWmVLrhe27JjS/bGezByLIkzH3YUaVpE\n" +
                "xKf7bVVfn7MTDDptV//H+uBRa2lMxvMCiZq48NbkNFpR/blr0FnBuL4pLkBvPMEp\n" +
                "9xeGZOTNDBWrXtXiIrNV0NEVqK1Tg5sgyhYbzYQn/ExzwqXgz/K905x71cLAOsbr\n" +
                "aKHyVTwVRMLZRjH44EOSVggDL3HwHeozG8C6rlDMsBi+sIM03zZN7fGvnOAtoul7\n" +
                "70xdNPdJIQKYoApl1N/GkQQBiF+z6Ujb8EhmDug5GoECgYEA0gLGbxwAua/DuJai\n" +
                "4UkLyXpY3GbWK0jAJDaaqyNC1LeYQO0H3lW4n7R3OuCMW+OEFmeWM0k85YokRtAT\n" +
                "IfS9nJ1RMnKCnodGdNJ197Zblhdc2/d4LMgdUqZ1m7kx2AhNQqtxvCEEgonxbyze\n" +
                "nIF3E2Ex5uZZTVbdi7TIVtxnBjECgYEAzh1FzzWUfy/t+CVK26327xd6j5OdhvyJ\n" +
                "JVgJFb8pEq6SWyfatF+eahuR4webHkVri3H8Sa0mD9iTuW0TgzyTFbiDISjtsIDN\n" +
                "ekU/NBdwRhGSmdShjy5V1ZD06Xdnl7YxWEBjsjD7vSpOXo1Y+Xa4hiLJpZABNJUO\n" +
                "BPxC5rrGP9kCgYAj71GhBXuiSk8FMNFiFyurKAI4hGg1M9ec2rMQgZIX7+pfga+M\n" +
                "/cH/odOdAXtC5eVeKA9tayS+airW6xe7AbPILqsMJMfw9Fi7+J8y6cM2JX1ALlIj\n" +
                "54IM+Gq7YK8EtjbyJ3/onwW0iIIFcfoNcaG8cS8mwVeagjiahS6anlAKoQKBgBP2\n" +
                "G8usibwjnS1aFzzdRTv0jycWibwH+xRFyWN0xvqLM5tmabTsScBG+UL+epRc7sRr\n" +
                "fp54CWahd/UOTkR4eloxK2nrt9FPOBcuW6ek2Xe/ve3t/NOMDj4HsuBlcv/rYi4E\n" +
                "1mLsgYTZcwuPLt3CFK7Q8ax6HNPF17y93ZDQ5+BJAoGACtd1TwGBUHbUTy9joyS8\n" +
                "YrIpR8EUk+xNw9iHxoohqbLoZJAA/bOdxnSVhvykjCa1161WJto3R0ekzVz+mKoK\n" +
                "1pvoAA0S8U9dzJafw41IKGMF9D3v91kUM0zIN8slubUK5849HVrwDyTE5/MAUdSf\n" +
                "dE1aWJycryFjoJQIIoqZMnw=";

        Map<String, String> paramsMap = new HashMap<>();

        paramsMap.put("syscode", syscode);
        paramsMap.put("account", account);
        paramsMap.put("trans_time", trans_time);
        paramsMap.put("amount", "100");
        paramsMap.put("pay_mode", "H5_CUWEB");
        paramsMap.put("app_id", trans_time);
        paramsMap.put("notify_url", notify_url);
        paramsMap.put("aging", "1");
        paramsMap.put("callback_url", callback_url);
        paramsMap.put("charset", "UTF8");
        paramsMap.put("memo", "测试");

        String signData = WebPay.getSignContent(paramsMap);

        paramsMap.put("sign", SignUtils.sign(signkey,signData));

        String respStr = HttpClientUtil.sendPost(url, paramsMap);

        System.out.println("响应报文："+respStr);

        if (respStr == null) {
            throw new Exception();
        }

        JSONObject resultJson = JSONObject.parseObject(respStr, Feature.OrderedField);

        if (resultJson != null && resultJson.getString("errorcode").equals("0000")) {

            //验签公钥
            String pubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoObbi56PJIdT2T2WRh8M\n" +
                    "CUuOrEqMHxGDJ8vL0I/BaTwZ+MZSSTq/1u1tpuVp1KP+7xXPX/mUpbQLexpYoRCr\n" +
                    "eGBTX3OZm+44tEDEBVqoyGXaYYwI6J0+fZqkryrzBDEPGkPUmpoyeJ9TGVcGm4DN\n" +
                    "IboTJ8KxsEADSrlcPvUDY6oEE9JsbHfdQv6PGu2xNVuz/uajq91A2eFBR9bWJbZq\n" +
                    "QzPW0K8vqDrtyIRXaQ7vLLwAD2kC/+Sfgk13DWm+Lv/KySlEhpD038SvJPOL+BVP\n" +
                    "0aocZQOX7GHdnGciZwfdNwVvPlPTVujZWD2yrh0VHLQdxc8/WllLydIHzLf/5XZJ\n" +
                    "RwIDAQAB";

            System.out.println("===验证报文："+resultJson.getString("response"));

            String verifyData = resultJson.getString("response");

            boolean verifyResult = SignUtils.verifySign(pubKey,verifyData,resultJson.getString("sign"));

            Map<String, String> respMap = new HashMap<>();
            respMap = parseResponse(resultJson.getString("response"));

            // 业务处理数据
            System.out.println("支付地址："+respMap.get("pay_url"));

        } else {
            System.out.println(resultJson.getString("errormessage"));
        }
    }

    private final static boolean isJSONValid(String jsonInString) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.readTree(jsonInString);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 遍历以及根据重新排序
     *
     * @param sortedParams
     * @return
     */
    public static String getSignContent(Map<String, String> sortedParams) {
        StringBuffer content = new StringBuffer();
        List<String> keys = new ArrayList<String>(sortedParams.keySet());
        Collections.sort(keys);
        int index = 0;
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = sortedParams.get(key);
            if (StringUtil.areNotEmpty(key, value)) {
                content.append((index == 0 ? "" : "&") + key + "=" + value);
                index++;
            }
        }
        return content.toString();
    }

    /**
     * 将获取到的response转换成json格式
     * @param response 获取到的返回数据
     * @return 返回值
     */
    public static Map<String, String> parseResponse(String response){

        Map<String,String> jsonMap  = new HashMap<>();
        jsonMap	= JSON.parseObject(response,
                new TypeReference<TreeMap<String,String>>() {});
        return jsonMap;
    }

    /**
     * GBK转UTF-8
     * @param gbkStr 转换
     * @return 返回值
     */
    public static byte[] getUTF8BytesFromGBKString(String gbkStr) {
        int n = gbkStr.length();
        byte[] utfBytes = new byte[3 * n];
        int k = 0;
        for (int i = 0; i < n; i++) {
            int m = gbkStr.charAt(i);
            if (m < 128 && m >= 0) {
                utfBytes[k++] = (byte) m;
                continue;
            }
            utfBytes[k++] = (byte) (0xe0 | (m >> 12));
            utfBytes[k++] = (byte) (0x80 | ((m >> 6) & 0x3f));
            utfBytes[k++] = (byte) (0x80 | (m & 0x3f));
        }
        if (k < utfBytes.length) {
            byte[] tmp = new byte[k];
            System.arraycopy(utfBytes, 0, tmp, 0, k);
            return tmp;
        }
        return utfBytes;
    }
}
