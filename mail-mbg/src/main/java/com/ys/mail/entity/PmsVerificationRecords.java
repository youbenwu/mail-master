package com.ys.mail.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 核销记录表
 * </p>
 *
 * @author 070
 * @since 2022-01-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="PmsVerificationRecords对象", description="核销记录表")
public class PmsVerificationRecords implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "核销记录id")
    @TableId(value = "record_id", type = IdType.INPUT)
    private Long recordId;

    @ApiModelProperty(value = "核销编码")
    private String code;

    @ApiModelProperty(value = "合伙人id")
    private Long partnerId;

    @ApiModelProperty(value = "订单id")
    private String orderId;

    @ApiModelProperty(value = "核销商品名称")
    private String productName;

    @ApiModelProperty(value = "核销商品价格")
    private Long productPrice;

    @ApiModelProperty(value = "核销商品图片")
    private String productPic;

    @ApiModelProperty(value = "总金额")
    private Long totalPrice;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "核销员名称")
    private String partnerName;

}
