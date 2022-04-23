package com.ys.mail.model.alipay;

import com.alibaba.fastjson.JSON;
import com.ys.mail.constant.FigureConstant;
import com.ys.mail.util.BlankUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author DT
 * @version 1.0
 * @date 2021-12-07 18:42
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class BusinessParams implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "付款方显示名称，可选，收款方在支付宝账单中可见。{\\\"payer_show_name\\\":\\\"xx公司\\\"}")
    private String payerShowName;

    public String getJsonString() {
        HashMap<String, String> map = new HashMap<>(FigureConstant.MAP_INIT_SIX_EEN);
        if(!BlankUtil.isEmpty(payerShowName)){
            map.put("payer_show_name",payerShowName);
        }
        return JSON.toJSONString(map);
    }
}
