package com.ys.mail.model.alipay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ys.mail.constant.AlipayConstant;
import com.ys.mail.constant.FigureConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author DT
 * @version 1.0
 * @date 2021-12-03 13:17
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="PayeeInfo对象", description="支付宝收款方信息")
public class PayeeInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "参与方的标识类型:ALIPAY_USER_ID：支付宝会员的用户 ID，可通过 获取会员信息 能力获取,ALIPAY_LOGON_ID：支付宝登录号，支持邮箱和手机号格式",required = true)
    private String identityType;

    @ApiModelProperty(value = "参与方的标识 ID,当 identity_type=ALIPAY_USER_ID 时，填写支付宝用户 UID。示例值：2088123412341234,当 identity_type=ALIPAY_LOGON_ID 时，填写支付宝登录号。示例值：186xxxxxxxx",required = true)
    private String identity;

    @ApiModelProperty(value = "参与方真实姓名,当 identity_type=ALIPAY_LOGON_ID 时，本字段必填")
    private String name;

    public String getJsonString() {
        HashMap<String, String> map = new HashMap<>(FigureConstant.MAP_INIT_SIX_EEN);
        map.put("identity_type",identityType);
        map.put("identity",identity);
        if(identityType.equals(AlipayConstant.IDENTITY_TYPE_LOGON)){
            map.put("name",name);
        }
        return JSON.toJSONString(map);
    }

}
