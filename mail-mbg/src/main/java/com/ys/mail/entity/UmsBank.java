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
 * 用户银行卡表,建议后台转1分审核是否成功
 * </p>
 *
 * @author 070
 * @since 2021-11-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="UmsBank对象", description="用户银行卡表,建议后台转1分审核是否成功")
public class UmsBank implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户银行卡主键id")
    @TableId(value = "bank_id", type = IdType.INPUT)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long bankId;

    @ApiModelProperty(value = "用户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    @ApiModelProperty(value = "银行卡名称")
    private String bankName;

    @ApiModelProperty(value = "银行卡编码")
    private String bankCode;

    @ApiModelProperty(value = "银行卡卡号")
    private String bankCard;

    @ApiModelProperty(value = "银行卡(背景颜色/背景图片)")
    private String bankImg;

    @ApiModelProperty(value = "所在地")
    private String bankAddress;

    @ApiModelProperty(value = "绑定银行卡的手机号")
    private String bankPhone;

    @ApiModelProperty(value = "银行卡类型:0为储蓄卡1为信用卡")
    @TableField("is_bank_type")
    private Boolean bankType;

    @ApiModelProperty(value = "银行卡是否被锁定:0->false,1->true")
    @TableField("is_lock_status")
    private Boolean lockStatus;

    @ApiModelProperty(value = "认证状态:0审核失败1审核成功2待审核")
    private Integer auditStatus;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "逻辑删除0->未删除,1删除")
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleted;


}
