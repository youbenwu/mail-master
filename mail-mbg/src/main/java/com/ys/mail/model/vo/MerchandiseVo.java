package com.ys.mail.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author 24
 * @date 2022/1/22 18:04
 * @description 上架库存Vo
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "上架库存Vo", description = "上架库存Vo")
public class MerchandiseVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private Long productId;

    @ApiModelProperty(value = "商品名称")
    private String productName;

    @ApiModelProperty(value = "主图")
    private String pic;

    @ApiModelProperty(value = "销量")
    private Integer sale;

    @ApiModelProperty(value = "价格,使用整数类型Long,1分就是1,100就是1元")
    private Long price;

    @ApiModelProperty(value = "库存")
    private Integer stock;

}
