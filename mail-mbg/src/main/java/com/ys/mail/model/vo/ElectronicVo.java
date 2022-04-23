package com.ys.mail.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 24
 * @date 2022/1/22 18:04
 * @description 上架库存Vo
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "电子券交易", description = "电子券交易Vo")
public class ElectronicVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private Long orderId;

    @ApiModelProperty(value = "支付状态")
    private String orderStatus;

    @ApiModelProperty(value = "商品名称")
    private String productName;

    @ApiModelProperty(value = "商品主图")
    private String productPic;

    @ApiModelProperty(value = "总价格")
    private Integer totalAmount;

    @ApiModelProperty(value = "购买数量")
    private Long productQuantity;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;




}
