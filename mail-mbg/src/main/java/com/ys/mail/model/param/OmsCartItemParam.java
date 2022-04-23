package com.ys.mail.model.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author DT
 * @version 1.0
 * @date 2021-12-30 10:43
 */
@Data
public class OmsCartItemParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品id",required = true)
    @NotBlank
    @Pattern(regexp = "^\\d{19}$")
    private String productId;

    @ApiModelProperty(value = "商品图片",required = true)
    @NotBlank
    private String productPic;

    @ApiModelProperty(value = "商品数量,默认为1")
    @NotNull
    private Integer quantity = 1;

    @ApiModelProperty(value = "商品销售属性:[{\"key\":\"颜色\",\"value\":\"颜色\"},{\"key\":\"容量\",\"value\":\"4G\"}]",required = true)
    @NotBlank
    private String productAttr;

    @ApiModelProperty(value = "商品价格",required = true)
    @NotNull
    @Min(value = 1)
    private Long pic;

    @ApiModelProperty(value = "商品skuId",required = true)
    @NotBlank
    @Pattern(regexp = "^\\d{19}$")
    private String productSkuId;
}
