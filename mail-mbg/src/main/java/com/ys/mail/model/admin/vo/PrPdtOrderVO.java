package com.ys.mail.model.admin.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author ghdhj
 */
@Data
public class PrPdtOrderVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单id")
    private Long orderId;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "付款总金额")
    private Long totalAmount;

    @ApiModelProperty(value = "数量")
    private Integer productQuantity;

    @ApiModelProperty(value = "合伙人商品主键id")
    private Long partnerPdtId;

    @ApiModelProperty(value = "合伙人商品名称")
    private String partnerName;

    @ApiModelProperty(value = "合伙人价格")
    private Long partnerPrice;

    @ApiModelProperty(value = "合伙人保证金")
    private Long earnestMoney;

    @ApiModelProperty(value = "合伙人总共金额:合伙人价格+保证金")
    private Long totalPrice;

    @ApiModelProperty(value = "返还期数1-12期")
    private Integer rePeriods;
}
