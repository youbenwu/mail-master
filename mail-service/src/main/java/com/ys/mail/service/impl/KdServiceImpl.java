package com.ys.mail.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ys.mail.domain.KdApiOrderDistinguish;
import com.ys.mail.exception.ApiAssert;
import com.ys.mail.exception.BusinessException;
import com.ys.mail.exception.code.BusinessErrorCode;
import com.ys.mail.service.KdService;
import com.ys.mail.service.RedisService;
import com.ys.mail.util.BlankUtil;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 用一句简单的话来描述下该类
 * 快递抽出来公共的service
 * @author DT
 * @date 2022-06-08 10:43
 * @since 1.0
 */
@Service
public class KdServiceImpl implements KdService {

    private final static Logger log = LoggerFactory.getLogger(KdServiceImpl.class);

    private static final String SF = "SF";
    private static final String SUCCESS = "Success";

    @Value("${redis.key.sysKdOrderNo}")
    private String sysKdOrderNo;
    @Autowired
    private RedisService redisService;
    @Autowired
    private KdApiOrderDistinguish kdApiOrderDistinguish;


    @SneakyThrows
    @Override
    public JSONObject logisticsTrack(String logisticCode, String shipperCode, String customerName) {
        // 公共抽出来实现类,单位顺丰快递时要输入手机号后四位
        String key = sysKdOrderNo + ":" + shipperCode + "_" + logisticCode;
        if (redisService.hasKey(key)) {
            return (JSONObject) redisService.get(key);
        }
        if(ObjectUtil.equal(SF,shipperCode) && BlankUtil.isEmpty(customerName)){
            throw new BusinessException(BusinessErrorCode.ERR_CUSTOMER_NAME_FOUR);
        }
        String response = kdApiOrderDistinguish.orderOnlineByJson(logisticCode,shipperCode,customerName);
        JSONObject result = JSONObject.parseObject(response);
        ApiAssert.noValue(result, BusinessErrorCode.ERR_KD_BIRD_NULL);
        if(!result.getBoolean(SUCCESS)){
            throw new BusinessException(result.getString("Reason"));
        }
        redisService.set(key, result, 43200);
        return result;
    }

    @SneakyThrows
    @Override
    public JSONArray getLogistics(String deliverySn) {
        JSONObject result = JSONObject.parseObject(kdApiOrderDistinguish.orderOnlineByJson(deliverySn));
        log.info(result.toString());
        JSONArray shippers = JSONObject.parseArray(result.getString("Shippers"));
        ApiAssert.noValue(shippers,BusinessErrorCode.ERR_KD_CODE);
        return shippers;
    }
}
