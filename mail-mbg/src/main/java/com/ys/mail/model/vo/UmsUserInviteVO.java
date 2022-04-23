package com.ys.mail.model.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ys.mail.entity.OmsOrderItem;
import com.ys.mail.entity.UmsUserInvite;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
@ApiModel(value="UmsUserInviteVO对象", description="用户邀请信息表")
public class UmsUserInviteVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "user_invite_id", type = IdType.INPUT)
    private Long userInviteId;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "上级id")
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

    @ApiModelProperty(value = "等级")
    private String level;

    @ApiModelProperty(value = "pid")
    private Long pid;


    @ApiModelProperty(value = "子节点")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<UmsUserInviteVO> children;

    @ApiModelProperty(value = "笔数")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private int number;

    @ApiModelProperty(value = "现有人数")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private int existing;

    @ApiModelProperty(value = "订单商品信息")
    private List<OmsOrderItem> omsOrderItem;

    @ApiModelProperty(value = "用户名称")
    private String nickName;
}
