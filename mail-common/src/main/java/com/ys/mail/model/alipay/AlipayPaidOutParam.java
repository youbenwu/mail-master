package com.ys.mail.model.alipay;

import com.alibaba.fastjson.JSON;
import com.ys.mail.constant.AlipayConstant;
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
 * 支付宝支出必传参数
 * @author DT
 * @version 1.0
 * @date 2021-12-07 17:36
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class AlipayPaidOutParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商家侧唯一订单号，由商家自定义。对于不同转账请求，商家需保证该订单号在自身系统唯一。示例值：20190619000000001",required = true)
    private String outBizNo;

    @ApiModelProperty(value = "订单总金额，单位为元，不支持千位分隔符，精确到小数点后两位，取值范围[0.1,100000000]。示例值：1.68",required = true)
    private String transAmount;

    @ApiModelProperty(value = "销售产品码。单笔无密转账固定为 TRANS_ACCOUNT_NO_PWD。",required = true)
    private String productCode;

    @ApiModelProperty(value = "业务场景。单笔无密转账固定为 DIRECT_TRANSFER。",required = true)
    private String bizScene;

    @ApiModelProperty(value = "收款方信息。",required = true)
    private PayeeInfo payeeInfo;

    @ApiModelProperty(value = "转账业务的标题，用于在支付宝用户的账单里显示。示例值：201905代发",required = true)
    private String orderTitle;

    @ApiModelProperty(value = "业务备注。示例值：201905代发")
    private String remark;

    @ApiModelProperty(value = "转账业务请求的扩展参数，支持传入的扩展参数如下：示例值：{\\\"payer_show_name\\\":\\\"xx公司\\\"}")
    private BusinessParams businessParams;


    public String getJsonString() {
        HashMap<String, String> map = new HashMap<>(FigureConstant.MAP_INIT_SIX_EEN);
        map.put("out_biz_no",outBizNo);
        map.put("trans_amount",transAmount);
        map.put("product_code", AlipayConstant.PRODUCT_CODE);
        map.put("biz_scene",AlipayConstant.BIZ_SCENE);
        map.put("order_title",orderTitle);
        map.put("payee_info",payeeInfo.getJsonString());
        if(!BlankUtil.isEmpty(remark)){
            map.put("remark",remark);
        }
        if(!BlankUtil.isEmpty(businessParams)){
            map.put("business_params",businessParams.getJsonString());
        }
        return JSON.toJSONString(map);
    }

}
