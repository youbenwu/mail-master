package com.ys.mail.model.admin.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * @Desc
 * @Author CRH
 * @Create 2021-12-29 21:30
 */
@Data
@ApiModel(value = "UmsUserBlackListParam", description = "用户黑名单参数")
public class UmsUserBlackListParam {

    @ApiModelProperty(value = "ID，0：表示新增，其他表示修改", required = true)
    @Pattern(regexp = "^(0|\\d{19})$")
    private String blId;

    @ApiModelProperty(value = "黑名单号码，该名单的号码不能注册登录")
    @Pattern(regexp = "^\\d{11}$")
    private String blPhone;

    @ApiModelProperty(value = "用户名称，方便后台查看")
    @Length(max = 5)
    private String blName;

    @ApiModelProperty(value = "是否启用，false->不启用，true->启用，默认启用")
    private Boolean enable;

    @ApiModelProperty(value = "备注")
    private String remark;

}
