package com.ys.mail.model.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

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
@ApiModel(value = "UmsUserInviterNumberPO对象", description = "用户ID对应的直接子级邀请人数对象")
public class UmsUserInviteNumberPO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "顶级邀请人对应的直接子级数量")
    private Long topInviteNumber;

    @ApiModelProperty(value = "父级邀请人对应的直接子级数量")
    private Long parentInviteNumber;

}
