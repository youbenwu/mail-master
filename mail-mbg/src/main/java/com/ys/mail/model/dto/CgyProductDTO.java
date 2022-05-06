package com.ys.mail.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 分类产品对象
 *
 * @author CRH
 * @date 2022-05-05 10:49
 * @since 1.0
 */
@Data
public class CgyProductDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "产品id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long productId;

    @ApiModelProperty(value = "商品价格")
    private Double price;

    @ApiModelProperty(value = "商品会员价格")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Double mebPrice;

    @ApiModelProperty(value = "销量")
    private Integer sale;

    @ApiModelProperty(value = "商品主图")
    private String pic;

    @ApiModelProperty(value = "商品名称")
    private String productName;

    @ApiModelProperty(value = "距离，单位m")
    private Double distance;

}
