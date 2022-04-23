package com.ys.mail.model.admin.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @version 1.0
 * @author： DT
 * @date： 2021-09-18 09:31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "PcUserRegisterParam", description = "后台用户参数")
public class PcUserRegisterParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "后台用户id，注册时不需要传")
    private String pcUserId;

    @ApiModelProperty(value = "用户名，登录账号，不能重复", required = true)
    @NotBlank
    @Size(min = 4, max = 32)
    private String username;

    @ApiModelProperty(value = "密码", required = true)
    @NotBlank
    @Size(min = 6, max = 32)
    private String password;

    @ApiModelProperty(value = "昵称，默认自动生成")
    private String nickname;

    @ApiModelProperty(value = "头像路径，为空默认头像")
    private String headPortrait;

}
