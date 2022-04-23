package com.ys.mail.service;

import com.ys.mail.model.CommonResult;

/**
 * 快递鸟apiService
 */
public interface KdNiaoService {

    /**
     * 即时查询接口
     * @param shipperCode 快递公司编码
     * @param logisticCode 物流单号
     * @return 返回值
     */
    CommonResult orderOnlineByJson(String shipperCode, String logisticCode);

    /**
     * 即时查询接口
     * @param customerName 寄件人/收件人手机号后四位
     * @param shipperCode 快递公司编码
     * @param logisticCode 物流单号
     * @return 返回值
     */
    CommonResult orderOnlineByJson(String customerName,String shipperCode,String logisticCode);
}
