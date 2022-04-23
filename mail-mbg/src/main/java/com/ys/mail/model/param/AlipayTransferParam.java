package com.ys.mail.model.param;

import com.ys.mail.model.alipay.PayeeInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 支付宝转账支付实体类
 * @author DT
 * @version 1.0
 * @date 2021-12-03 11:36
 */
@Data
public class AlipayTransferParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商家侧唯一订单号，由商家自定义。对于不同转账请求，商家需保证该订单号在自身系统唯一,示例值：20190619000000001")
    private String outBizNo;

    @ApiModelProperty(value = "订单总金额，单位为元，不支持千位分隔符，精确到小数点后两位，取值范围[0.1,100000000]")
    private String transAmount;

    @ApiModelProperty(value = "销售产品码。单笔无密转账固定为 TRANS_ACCOUNT_NO_PWD")
    private String productCode;

    @ApiModelProperty(value = "业务场景。单笔无密转账固定为 DIRECT_TRANSFER")
    private String bizScene;

    @ApiModelProperty(value = "收款方信息")
    private PayeeInfo payeeInfo;

    @ApiModelProperty(value = "转账业务的标题，用于在支付宝用户的账单里显示")
    private String orderTitle;

    @ApiModelProperty(value = "业务备注")
    private String remark;

    @ApiModelProperty(value = "转账业务请求的扩展参数")
    private String businessParams;
}
