package com.ys.mail.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author DT
 * @version 1.0
 * @date 2021-12-12 17:58
 */
@Data
public class OrderItemSkuVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "sku的库存")
    private Integer stock;

    @ApiModelProperty(value = "价格")
    private String productPrice;

    @ApiModelProperty(value = "购买的商品数量")
    private Integer productQuantity;
}
