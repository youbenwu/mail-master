package com.ys.mail.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 用户邀请佣金规则表
 * </p>
 *
 * @author 070
 * @since 2021-11-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "UmsUserInviteRule对象", description = "用户邀请佣金规则表")
public class UmsUserInviteRule implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "invite_rule_id", type = IdType.INPUT)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long inviteRuleId;

    @ApiModelProperty(value = "代理等级")
    private Integer agentLevel;

    @ApiModelProperty(value = "最小金额")
    private BigDecimal minTotalAmount;

    @ApiModelProperty(value = "最大金额")
    private BigDecimal maxTotalAmount;

    @ApiModelProperty(value = "佣金比例")
    private BigDecimal commissionRate;

    @ApiModelProperty(value = "分佣规则类型：0表示邀请代理分佣比例，1表示成为团长最低人数")
    private Integer incomeType;

    @ApiModelProperty(value = "描述")
    @TableField(value = "`describe`")
    private String describe;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;


}
