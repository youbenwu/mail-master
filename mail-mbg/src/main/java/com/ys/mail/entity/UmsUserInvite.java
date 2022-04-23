package com.ys.mail.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ys.mail.model.admin.tree.PcMenuTree;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 用户邀请信息表
 * </p>
 *
 * @author 070
 * @since 2021-11-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="UmsUserInvite对象", description="用户邀请信息表")
public class UmsUserInvite implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "user_invite_id", type = IdType.INPUT)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userInviteId;

    @ApiModelProperty(value = "用户id")
    @NotNull
    private Long userId;

    @ApiModelProperty(value = "上级id")
    @NotNull
    private Long parentId;

    @ApiModelProperty(value = "佣金奖励")
    private Long commissionReward;

    @ApiModelProperty(value = "社团奖励")
    private Long communityReward;

    @ApiModelProperty(value = "社团人数")
    private Integer associationsNums;

    @ApiModelProperty(value = "邀请时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}
