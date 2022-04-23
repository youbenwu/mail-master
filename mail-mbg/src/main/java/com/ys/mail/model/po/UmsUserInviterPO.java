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
@ApiModel(value = "UmsUserInviterPO对象", description = "用户的上级邀请者对象")
public class UmsUserInviterPO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "当前用户ID")
    private Long userId;

    @ApiModelProperty(value = "当前用户顶级邀请人的ID")
    private Long topInviteId;

    @ApiModelProperty(value = "当前用户父级邀请人的ID")
    private Long parentInviteId;

}
