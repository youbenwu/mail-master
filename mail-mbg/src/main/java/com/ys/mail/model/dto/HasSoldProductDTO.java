package com.ys.mail.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author DT
 * @version 1.0
 * @date 2022-01-14 10:12
 */
@Data
public class HasSoldProductDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 取用订单详情+限时购部分详情,数据不宜过多
     */

    @ApiModelProperty(value = "订单id")
    private Long orderId;

    @ApiModelProperty(value = "实际收入价")
    private Long totalAmount;

    @ApiModelProperty(value = "秒杀价")
    private Long payAmount;

    @ApiModelProperty(value = "商品主图")
    private String productPic;

    @ApiModelProperty(value = "商品名称")
    private String productName;

    @ApiModelProperty(value = "秒杀发布价")
    private Long flashPromotionPrice;

    @ApiModelProperty(value = "收货人姓名")
    private String receiverName;

    @ApiModelProperty(value = "收货人手机号")
    private String receiverPhone;

    @ApiModelProperty(value = "详细地址")
    private String receiverAddress;

    @ApiModelProperty(value = "订单编号")
    private String orderSn;

    @ApiModelProperty(value = "支付方式：0->未支付；1云闪付，2支付宝")
    private Integer payType;

    @ApiModelProperty(value = "支付时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date paymentTime;

    @ApiModelProperty(value = "物流公司")
    private String deliveryCompany;

    @ApiModelProperty(value = "订单状态：0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单；6->已付款；7->待评价")
    private Integer orderStatus;

}
