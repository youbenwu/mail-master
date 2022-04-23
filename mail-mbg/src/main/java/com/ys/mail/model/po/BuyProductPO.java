package com.ys.mail.model.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author DT
 * @version 1.0
 * @date 2021-11-29 11:07
 */
@Data
public class BuyProductPO implements Serializable {

    @ApiModelProperty(value = "商品id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long productId;

    @ApiModelProperty(value = "商品分类id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long pdtCgyId;

    @ApiModelProperty(value = "商品名称")
    private String productName;

    @ApiModelProperty(value = "商品主图")
    private String pic;

    @ApiModelProperty(value = "sku_id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long skuStockId;

    @ApiModelProperty(value = "数量")
    private Integer quantity;

    @ApiModelProperty(value = "商品销售属性")
    private String spData;

    @ApiModelProperty(value = "商品价格,1:100,1元就是代表100,普通价格")
    private Long price;

    @ApiModelProperty(value = "会员价")
    private Long mebPrice;

    @ApiModelProperty(value = "折扣")
    private BigDecimal disCount;
}
