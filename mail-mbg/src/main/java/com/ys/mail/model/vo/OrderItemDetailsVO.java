package com.ys.mail.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

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
@ApiModel(value="OmsOrderItemVO对象", description="订单中所包含的商品")
public class OrderItemDetailsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long orderItemId;

    @ApiModelProperty(value = "订单ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long orderId;

    @ApiModelProperty(value = "商品规格")
    private String spData;

    @ApiModelProperty(value = "存储的整数类型,1元就是100")
    private Long productPrice;

    @ApiModelProperty(value = "购买数量")
    private Integer productQuantity;

    @ApiModelProperty(value = "商品名称")
    private String productName;

    @ApiModelProperty(value = "商品图片")
    private String productPic;

    @ApiModelProperty(value = "是否评价,商品在已完成收货后评价就是1，未评价就是0")
    @TableField("is_appraise")
    private Boolean appraise;

    @ApiModelProperty(value = "商品Id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long productId;

}
