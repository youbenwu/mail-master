package com.ys.mail.model.admin.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Desc 订单查询对象
 * @Author CRH
 * @Create 2021-12-27 13:48
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserBlackListQuery extends Query {

    @ApiModelProperty(value = "黑名单号码，该名单的号码不能注册登录")
    private String blPhone;

    @ApiModelProperty(value = "用户名称，方便后台查看")
    private String blName;

    @ApiModelProperty(value = "是否启用，false->不启用，true->启用，默认启用")
    private Boolean enable;

}


