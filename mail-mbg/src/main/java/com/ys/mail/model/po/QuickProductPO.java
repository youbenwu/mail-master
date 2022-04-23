package com.ys.mail.model.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author DT
 * @version 1.0
 * @date 2021-12-22 09:48
 */
@Data
public class QuickProductPO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long productId;

    @ApiModelProperty(value = "商品分类id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long pdtCgyId;

    @ApiModelProperty(value = "商品名称")
    private String productName;

    @ApiModelProperty(value = "商品主图,存的sku主图")
    private String pic;

    @ApiModelProperty(value = "sku_id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long skuStockId;

    @ApiModelProperty(value = "数量,当前限制为1件")
    private Integer quantity = 1;

    @ApiModelProperty(value = "商品销售属性")
    private String spData;

    @ApiModelProperty(value = "当前发布的秒杀价格")
    private Long price;

    // 秒杀表，库存以秒杀表为准,比如sku当中有,在对象中最好只有一个库存,否则你要修改两次,update,sku主要做,后台管理系统发布100件秒杀那么sku对应减去100
}
