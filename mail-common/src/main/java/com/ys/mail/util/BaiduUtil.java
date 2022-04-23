package com.ys.mail.util;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 24
 * @date 2022/1/25 13:26
 * @description
 */
public class BaiduUtil {

    //todo : 接口是百度地理位置编码   地址:https://lbsyun.baidu.com/index.php?title=webapi/guide/webservice-geocoding
    public static String ak="Viof6ZDwMMUsYGF69wY5QWGi2qh8WcXH";

    public static void main(String[] args) {
        Map<String, Double> map = getLngAndLat("广东省广州市天河区");
        Double lng = map.get("lng");
        Double lat = map.get("lat");
        System.out.println(lng);
        System.out.println(lat);
    }

    /**
     * Baidu地图通过地址获取经纬度
     */
    public static Map<String, Double> getLngAndLat(String address) {

        Map<String, Double> map = new HashMap<String, Double>();
        address = address.replace(" ", "");
        String url = "http://api.map.baidu.com/geocoding/v3/?address=" + address
                + "&output=json&ak=" + ak + "&callback=showLocation";
        try {
            String json = loadJSON(url);

            json = json.replace("showLocation&&showLocation(", "");
            json = json.substring(0, json.length() - 1);
            JSONObject obj = JSONObject.parseObject(json);
            if (obj.get("status").toString().equals("0")) {
                double lng = obj.getJSONObject("result").getJSONObject("location").getDouble("lng");
                double lat = obj.getJSONObject("result").getJSONObject("location").getDouble("lat");
                map.put("lng", lng);
                map.put("lat", lat);
            }
        } catch (Exception e) {
            System.out.println("未找到相匹配的经纬度，请检查地址！");
        }
        return map;
    }

    private static String loadJSON(String url) {
        StringBuilder json = new StringBuilder();
        try {
            URL oracle = new URL(url);
            URLConnection yc = oracle.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream(), StandardCharsets.UTF_8));
            String inputLine = null;
            while ((inputLine = in.readLine()) != null) {
                json.append(inputLine);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json.toString();
    }
}
