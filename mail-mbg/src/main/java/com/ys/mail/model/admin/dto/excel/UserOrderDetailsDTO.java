package com.ys.mail.model.admin.dto.excel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Desc 用户个人订单详情数据
 * @Author CRH
 * @Create 2022-03-30 16:44
 */
@Data
@ApiModel(value = "UserOrderDetailsDTO")
public class UserOrderDetailsDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "下单支付账号列表")
    private Long orderId;

    @ApiModelProperty(value = "支付金额")
    private Double money;

    @ApiModelProperty(value = "支付类型")
    private Integer payType;

    @ApiModelProperty(value = "订单状态")
    private Integer orderStatus;

    @ApiModelProperty(value = "订单类型")
    private Integer orderType;

    @ApiModelProperty(value = "下单时间")
    private Date createTime;

    @ApiModelProperty(value = "支付时间")
    private Date paymentTime;

    @ApiModelProperty(value = "收货状态")
    private Integer confirmStatus;

    @ApiModelProperty(value = "交易流水号")
    private String transId;

    @ApiModelProperty(value = "客户端类型")
    private Integer cpyType;

    @ApiModelProperty(value = "订单备注")
    private String orderNote;

    @ApiModelProperty(value = "商品名称")
    private String productName;

}
