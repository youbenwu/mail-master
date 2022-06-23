package com.ys.mail.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 订单中所包含的商品
 * </p>
 *
 * @author 070
 * @since 2021-11-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "OmsOrderItemVO对象", description = "订单中所包含的商品")
public class OmsOrderItemVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long orderItemId;

    @ApiModelProperty(value = "订单id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long orderId;

    @ApiModelProperty(value = "商品Id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long productId;

    @ApiModelProperty(value = "商品分类名称")
    private String pdtCgyName;

    @ApiModelProperty(value = "订单编号")
    private String orderSn;

    @ApiModelProperty(value = "产品属性分类名称")
    private String pdtAttributeCgyName;

    @ApiModelProperty(value = "商品主图")
    private String productPic;

    @ApiModelProperty(value = "商品名称")
    private String productName;

    @ApiModelProperty(value = "商品品牌")
    private String productBrand;

    @ApiModelProperty(value = "商品货号")
    private String productSn;

    @ApiModelProperty(value = "商品价格,1元就是100")
    private Long productPrice;

    @ApiModelProperty(value = "购买数量")
    private Integer productQuantity;

    @ApiModelProperty(value = "商品sku编号")
    private Long productSkuId;

    @ApiModelProperty(value = "商品销售属性:[{\"key\":\"颜色\",\"value\":\"颜色\"},{\"key\":\"容量\",\"value\":\"4G\"}]")
    private String spData;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "是否评价,商品在已完成收货后评价就是1，未评价就是0")
    private Boolean appraise;

}
