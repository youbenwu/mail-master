package com.ys.mail.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author DT
 * @version 1.0
 * @date 2021-12-30 16:00
 */
@Data
public class QuickOrderDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单id")
    private String orderId;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "订单编号")
    private String orderSn;

    @ApiModelProperty(value = "金额")
    private Long totalAmount;

    @ApiModelProperty(value = "支付方式：0->未支付；1云闪付,2支付宝")
    private Byte payType;

    @ApiModelProperty(value = "订单状态：0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单")
    private Byte orderStatus;

    @ApiModelProperty(value = "0->正常订单；1->秒杀订单;2->拼团订单")
    private Byte orderType;

    @ApiModelProperty(value = "商品图片")
    private String productPic;

    @ApiModelProperty(value = "商品名称")
    private String productName;

    @ApiModelProperty(value = "数量")
    private Byte productQuantity;

    @ApiModelProperty(value = "商品属性")
    private String productAttr;

    @ApiModelProperty(value = "合伙人id")
    private Long partnerId;

    @ApiModelProperty(value = "状态 0待使用 1已使用 2已失效")
    private Integer isStatus;

    @ApiModelProperty(value = "核销编码")
    private String code;
}
