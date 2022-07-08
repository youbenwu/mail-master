package com.ys.mail.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * app用户表
 * </p>
 *
 * @author 070
 * @since 2021-11-09
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "UmsUser对象", description = "app用户表")
public class UmsUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
    @TableId(value = "user_id", type = IdType.INPUT)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "头像")
    private String headPortrait;

    @ApiModelProperty(value = "微信OPENID")
    private String openid;

    @ApiModelProperty(value = "角色:0普通用户用户,1高级用户")
    private Integer roleId;

    @ApiModelProperty(value = "0->禁用；1->启用")
    @TableField("is_user_status")
    private Boolean userStatus;

    @ApiModelProperty(value = "积分总数量")
    private Long integralSum;

    @ApiModelProperty(value = "支付宝姓名")
    private String alipayName;

    @ApiModelProperty(value = "支付宝账号")
    private String alipayAccount;

    @ApiModelProperty(value = "支付密码")
    private String payPassword;

    @ApiModelProperty(value = "升级支付类型：0->升级会员-未付款；1->升级会员-已付款")
    private Integer paymentType;

    @ApiModelProperty(value = "人脸肖像数据，采用Hash512压缩后再对比")
    private String userImageString;

    @ApiModelProperty(value = "礼品；0->未领取；1->已领取")
    @TableField("is_exchange_gift")
    private Boolean exchangeGift;

    @ApiModelProperty(value = "0->不活跃 1->活跃")
    private Integer isActive;

    @ApiModelProperty(value = "逻辑删除：0->未删除；1->删除")
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleted;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "上级id（暂不用）")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long parentId;

    @ApiModelProperty(value = "邀请时间（暂不用）", notes = "注意：只能在邀请时主动更新一次，之后不能再更新")
    private Date inviteTime;

    @ApiModelProperty(value = "游客登录的唯一uuid")
    private String uuid;

    /**
     * 设置支付密码构造器
     *
     * @param userId      用户id
     * @param payPassword 密码
     */
    public UmsUser(Long userId, String payPassword) {
        this.userId = userId;
        this.payPassword = payPassword;
    }
}
