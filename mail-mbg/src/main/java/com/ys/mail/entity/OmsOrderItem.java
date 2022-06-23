package com.ys.mail.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

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
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "OmsOrderItem对象", description = "订单中所包含的商品")
public class OmsOrderItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "order_item_id", type = IdType.INPUT)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long orderItemId;

    @ApiModelProperty(value = "订单id")
    private Long orderId;

    @ApiModelProperty(value = "商品id")
    private Long productId;

    @ApiModelProperty(value = "商品分类id")
    private Long pdtCgyId;

    @ApiModelProperty(value = "订单编号")
    private String orderSn;

    @ApiModelProperty(value = "商品主图")
    private String productPic;

    @ApiModelProperty(value = "商品名称")
    private String productName;

    @ApiModelProperty(value = "商品品牌")
    private String productBrand;

    @ApiModelProperty(value = "商品货号")
    private String productSn;

    @ApiModelProperty(value = "产品属性分类名称")
    private String pdtAttributeCgyName;

    @ApiModelProperty(value = "存储的整数类型,1元就是100")
    private Long productPrice;

    @ApiModelProperty(value = "购买数量")
    private Integer productQuantity;

    @ApiModelProperty(value = "商品sku编号")
    private Long productSkuId;

    @ApiModelProperty(value = "商品sku条码")
    private String productSkuCode;

    @ApiModelProperty(value = "商品促销名称")
    private String promotionName;

    @ApiModelProperty(value = "商品促销分解金额,1元就是100")
    private Long promotionAmount;

    @ApiModelProperty(value = "优惠券优惠分解金额,1元就是100")
    private Long couponAmount;

    @ApiModelProperty(value = "积分优惠分解金额,1元就是100")
    private Long integrationAmount;

    @ApiModelProperty(value = "该商品经过优惠后的分解金额,1元就是100")
    private Long realAmount;

    @ApiModelProperty(value = "商品销售属性:[{\"key\":\"颜色\",\"value\":\"颜色\"},{\"key\":\"容量\",\"value\":\"4G\"}]")
    private String productAttr;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "是否评价,商品在已完成收货后评价就是1，未评价就是0")
    @TableField("is_appraise")
    private Boolean appraise;

    @ApiModelProperty(value = "合伙人商品主键id")
    private Long partnerPdtId;

}
