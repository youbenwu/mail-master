package com.ys.mail.model.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author DT
 * @version 1.0
 * @date 2021-12-15 17:20
 */
@Data
public class DepositAlipayParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "总金额,最低0.1",required = true)
    @NotBlank
    @Pattern(regexp = "^([1-9]\\d*|0)(\\.\\d{1,2})?$",message = "请输入正确的金额")
    private String transAmount;

    @ApiModelProperty(value = "姓名",required = true)
    @NotBlank
    @Pattern(regexp = "^(?:[\\u4e00-\\u9fa5·]{2,16})$",message = "请输入正确的支付宝姓名")
    private String alipayName;

    @ApiModelProperty(value = "支付密码",required = true)
    @NotBlank
    @Pattern(regexp = "^\\d{6}$",message = "支付密码不少于6位")
    private String payPassword;

}
