package com.ys.mail.model.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author DT
 * @version 1.0
 * @date 2021-12-15 17:05
 */
@Data
public class BindAlipayParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "支付宝姓名",required = true)
    @NotBlank
    @Pattern(regexp = "^[\\u4e00-\\u9fa5·]{2,16}$",message = "请输入正确的支付宝姓名")
    private String alipayName;

    @ApiModelProperty(value = "支付宝账号",required = true)
    @NotBlank
    @Pattern(regexp = "^(?:1[3-9]\\d{9}|[a-zA-Z\\d._-]*@[a-zA-Z\\d.-]{1,10}\\.[a-zA-Z\\d]{1,20})$",message = "请输入正确的支付宝账号")
    private String alipayAccount;

    @ApiModelProperty(value = "验证码",required = true)
    @NotBlank
    @Pattern(regexp = "^\\d{6}$",message = "请输入6位数字的验证码")
    private String authCode;
}
