package com.ys.mail.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author 070
 * @since 2022-01-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="UmsUserBlacklist对象", description="")
public class UmsUserBlacklist implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "黑名单ID")
    @TableId(value = "bl_id", type = IdType.INPUT)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long blId;

    @ApiModelProperty(value = "黑名单号码，该名单的号码不能注册登录")
    private String blPhone;

    @ApiModelProperty(value = "用户名称，方便后台查看")
    private String blName;

    @ApiModelProperty(value = "是否启用，0->不启用，1->启用，默认启用")
    @TableField("is_enable")
    private Boolean enable;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "修改者用户id")
    private Long pcUserId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}
