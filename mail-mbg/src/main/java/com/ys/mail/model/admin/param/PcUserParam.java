package com.ys.mail.model.admin.param;

import com.ys.mail.annotation.BlankOrPattern;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
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
@ApiModel(value = "PcUserParam", description = "后台用户参数")
public class PcUserParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "后台用户id", required = true)
    @Pattern(regexp = "^\\d{19}$", message = " 长度必须为19位，请检查！")
    private String pcUserId;

    @ApiModelProperty(value = "用户名，登录账号，不能重复")
    private String username;

    @ApiModelProperty(value = "密码，格式：支持数字、大小写字母、任意符号")
    @BlankOrPattern(regexp = "[\\da-zA-Z\\s\\S]{6,32}", message = "长度范围是6~32，请检查！")
    private String password;

    @ApiModelProperty(value = "昵称，默认自动生成")
    private String nickname;

    @ApiModelProperty(value = "头像路径，为空默认头像")
    private String headPortrait;

}
