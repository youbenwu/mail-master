package com.ys.mail.model.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 用户设置支付密码
 * @author DT
 * @version 1.0
 * @date 2021-12-15 15:16
 */
@Data
public class UserTemPayCodeParam implements Serializable {

    @ApiModelProperty(value = "密码,第一次输入",required = true)
    @NotBlank
    @Pattern(regexp = "^\\d{6}$")
    private String newPassword;

    @ApiModelProperty(value = "密码,第二次输入",required = true)
    @NotBlank
    @Pattern(regexp = "^\\d{6}$")
    private String oldPassword;

    @ApiModelProperty(value = "随机码,安全验证获取",required = true)
    @NotBlank
    private String randomCode;
}
