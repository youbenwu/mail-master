package com.ys.mail.model.po;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 商品品牌信息
 *
 * @author CRH
 * @date 2022-04-19 15:19
 * @since 1.0
 */
@Data
public class ProductAndBrandPO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品ID")
    private Long productId;

    @ApiModelProperty(value = "货号")
    private String productSn;

    @ApiModelProperty(value = "名称")
    private String brandName;

    @ApiModelProperty(value = "产品属性分类名称")
    private String name;
}
