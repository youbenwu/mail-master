package com.ys.mail.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author 070
 * @since 2022-01-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="OmsVerificationOrder对象", description="")
public class OmsVerificationOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单核销码id")
    @TableId(value = "verification_order_id", type = IdType.INPUT)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long verificationOrderId;

    @ApiModelProperty(value = "核销码id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long verificationId;
    // 真实放入的是:orderSn
    @ApiModelProperty(value = "订单id")
    private String orderId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "逻辑删除：0->未删除,1->删除")
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleted;

    @ApiModelProperty(value = "数量")
    private Integer quantity;


}
