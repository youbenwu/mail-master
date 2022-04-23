package com.ys.mail.model.admin.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Desc APP用户管理黑名单列表
 * @Author CRH
 * @Create 2022-02-11 19:51
 */
@Data
@ApiModel(value = "UmsUserBlackListVO", description = "APP用户管理对象")
public class UmsUserBlackListVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "头像")
    private String headPortrait;

    @ApiModelProperty(value = "微信OPENID")
    private String openid;

    @ApiModelProperty(value = "角色:0普通用户用户,1高级用户")
    private Integer roleId;

    @ApiModelProperty(value = "0->禁用；1->启用")
    private Boolean userStatus;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "逻辑删除：0->未删除；1->删除")
    private Integer deleted;

    @ApiModelProperty(value = "积分总数量")
    private Long integralSum;

    @ApiModelProperty(value = "支付宝姓名")
    private String alipayName;

    @ApiModelProperty(value = "支付宝账号")
    private String alipayAccount;

    @ApiModelProperty(value = "升级支付类型：0->升级会员-未付款；1->升级会员-已付款")
    private Integer paymentType;

    @ApiModelProperty(value = "礼品；0->未领取；1->已领取")
    private Boolean exchangeGift;

    @ApiModelProperty(value = "0->不活跃 1->活跃")
    private Integer isActive;

    @ApiModelProperty(value = "黑名单ID")
    private String blId;

    @ApiModelProperty(value = "黑名单状态：是否启用，0->不启用，1->启用，默认启用")
    private Boolean blEnable;

    @ApiModelProperty(value = "黑名单备注")
    private String blRemark;
}
