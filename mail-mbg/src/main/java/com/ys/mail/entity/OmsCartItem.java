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
 * 购物车表
 * </p>
 *
 * @author 070
 * @since 2021-11-16
 */

@Data@Builder@AllArgsConstructor@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="OmsCartItem对象", description="购物车表")
public class OmsCartItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "cart_item_id", type = IdType.INPUT)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long cartItemId;

    @ApiModelProperty(value = "商品id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long productId;

    @ApiModelProperty(value = "商品库存id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long productSkuId;

    @ApiModelProperty(value = "用户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    @ApiModelProperty(value = "购买数量")
    private Integer quantity;

    @ApiModelProperty(value = "添加到购物车的价格,1就是1分")
    private Long price;

    @ApiModelProperty(value = "商品sku条码")
    private String productSkuCode;

    @ApiModelProperty(value = "商品货号")
    private String productSn;

    @ApiModelProperty(value = "商品销售属性:{key:颜色,value:颜色},{key:容量,value:4G}")
    private String productAttr;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "商品名称")
    private String productName;

    @ApiModelProperty(value = "商品图片")
    private String productPic;

    @ApiModelProperty(value = "商品分类id")
    private Long pdtCgyId;
}
