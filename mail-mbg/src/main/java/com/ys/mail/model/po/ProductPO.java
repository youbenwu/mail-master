package com.ys.mail.model.po;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
/**
 * @author ghdhj
 */
@Data
public class ProductPO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品id,首次传空翻首页,后面传最后一个id")
    private Long productId;

    @ApiModelProperty(value = "图片")
    private String pic;

    @ApiModelProperty(value = "商品名称")
    private String productName;

    @ApiModelProperty(value = "价格")
    private Long price;

    @ApiModelProperty(value = "销量")
    private Integer sale;

    @ApiModelProperty(value = "会员价")
    private Long mebPrice;

}
