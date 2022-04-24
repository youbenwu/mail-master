package com.ys.mail.util;

import com.alibaba.fastjson.JSONObject;
import com.ys.mail.constant.StringConstant;
import com.ys.mail.exception.ApiException;
import com.ys.mail.model.map.MapDataDTO;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

/**
 * 百度地图服务工具
 *
 * @author CRH
 * @date 2022-04-19 15:19
 * @since 1.0
 */
public class BaiduMapUtil {

    /**
     * 接口是百度地理服务
     * 地址:https://lbsyun.baidu.com/index.php?title=webapi/guide/webservice-geocoding
     */
    private static final String MAP_API_URL = "https://api.map.baidu.com";
    /**
     * 地理正编码服务
     */
    private static final String GEOCODER = "geocoding";
    private static final String VERSION = "v3";
    public static String AK = "Viof6ZDwMMUsYGF69wY5QWGi2qh8WcXH";

    /**
     * Baidu地图通过地址获取经纬度
     *
     * @param address 需要查找的地址，尽量完整结构化
     * @return 地图数据
     */
    public static MapDataDTO getLngAndLat(String address) {
        address = address.replace(" ", "");
        // 组装请求路径
        String url = String.format("%s/%s/%s/?address=%s&output=json&ak=%s", MAP_API_URL, GEOCODER, VERSION, address, AK);
        String json = loadJson(url);
        // 解析结果
        JSONObject obj = JSONObject.parseObject(json);
        if (BlankUtil.isNotEmpty(obj) && StringConstant.ZERO.equals(obj.get(StringConstant.RESULT_STATUS).toString())) {
            JSONObject location = obj.getJSONObject("result").getJSONObject("location");
            double lat = location.getDouble("lat");
            double lng = location.getDouble("lng");
            return MapDataDTO.builder().lat(lat).lng(lng).build();
        } else {
            throw new ApiException(obj.toString());
        }
    }

    private static String loadJson(String url) {
        StringBuilder json = new StringBuilder();
        try {
            URL oracle = new URL(url);
            URLConnection yc = oracle.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream(), StandardCharsets.UTF_8));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                json.append(inputLine);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    public static void main(String[] args) {
        MapDataDTO mapDataDTO = getLngAndLat("尚北科创园");
        if (BlankUtil.isNotEmpty(mapDataDTO)) {
            System.out.println(mapDataDTO.getLat());
            System.out.println(mapDataDTO.getLng());
        }
    }
}
