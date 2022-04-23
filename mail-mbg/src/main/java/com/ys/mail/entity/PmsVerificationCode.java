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
@ApiModel(value="PmsVerificationCode对象", description="")
public class PmsVerificationCode implements Serializable {

    private static final long serialVersionUID = 1L;

    /* 待使用  */
    public static final Integer CODE_STATUS_ZERO = 0;
    /* 已使用 */
    public static final Integer CODE_STATUS_ONE = 1;
    /*  失效-----确认交付不可使用 */
    public static final Integer CODE_STATUS_TWO = 2;

    @ApiModelProperty(value = "核销码id")
    @TableId(value = "verification_id", type = IdType.INPUT)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long verificationId;

    @ApiModelProperty(value = "合伙人id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long partnerId;

    @ApiModelProperty(value = "核销编码")
    private String code;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "状态 0待使用 1已使用 2已失效")
    private Integer isStatus;

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


}
