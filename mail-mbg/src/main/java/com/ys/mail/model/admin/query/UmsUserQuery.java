package com.ys.mail.model.admin.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Desc
 * @Author CRH
 * @Create 2022-02-11 20:03
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UmsUserQuery extends Query implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "角色:0普通用户用户,1高级用户")
    private Integer roleId;

    @ApiModelProperty(value = "false->禁用；true->启用")
    private Boolean userStatus;

    @ApiModelProperty(value = "支付宝姓名")
    private String alipayName;

    @ApiModelProperty(value = "支付宝账号")
    private String alipayAccount;

    @ApiModelProperty(value = "升级支付类型：0->升级会员-未付款；1->升级会员-已付款")
    private Boolean paymentType;
}
