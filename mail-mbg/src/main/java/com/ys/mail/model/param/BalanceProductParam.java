package com.ys.mail.model.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author DT
 * @version 1.0
 * @date 2021-12-12 16:32
 */
@Data
public class BalanceProductParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单编号", required = true)
    @Pattern(regexp = "^\\d+$")
    @NotBlank
    private String orderSn;

    @ApiModelProperty(value = "支付金额", required = true)
    @Pattern(regexp = "^\\d{1,}$")
    @NotBlank
    private String amount;

    @ApiModelProperty(value = "用户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    @ApiModelProperty(value = "姓名", required = true)
    @NotBlank
    @Pattern(regexp = "^(?:[\\u4e00-\\u9fa5·]{2,16})$", message = "请输入正确的支付宝姓名")
    private String alipayName;

    @ApiModelProperty(value = "支付密码", required = true)
    @NotBlank
    @Pattern(regexp = "^\\d{6}$", message = "支付密码不少于6位")
    private String payPassword;

}
