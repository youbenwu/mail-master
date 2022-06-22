package com.ys.mail.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 用一句简单的话来描述下该类
 *
 * @author DT
 * @date 2022-06-08 10:43
 * @since 1.0
 */
public interface KdService {

    /**
     * 查询物流轨迹
     * @param logisticCode 物流单号
     * @param shipperCode 快递公司编码
     * @param customerName 手机号后四位
     * @return 返回值
     */
    JSONObject logisticsTrack(String logisticCode, String shipperCode, String customerName);

    /**
     * 根据物流单号获取基本信息
     * @param deliverySn 单号
     * @return 返回值
     */
    JSONArray getLogistics(String deliverySn);
}
