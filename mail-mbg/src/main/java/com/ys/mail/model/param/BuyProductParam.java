package com.ys.mail.model.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author DT
 * @version 1.0
 * @date 2021-12-12 16:32
 */
@Data
public class BuyProductParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单编号", required = true)
    @Pattern(regexp = "^\\d+$")
    @NotBlank
    private String orderSn;

    @ApiModelProperty(value = "支付金额", required = true)
    @Pattern(regexp = "^\\d+$")
    @NotBlank
    private String amount;

    @ApiModelProperty(value = "公司类型:0大尾狐->吉狐科技,1呼啦兔->桔狐科技", required = true)
    @NotNull
    @Range(min = 0, max = 1)
    private Byte cpyType;

    @ApiModelProperty(value = "支付类型:0:云闪付app,1:微信app,2:支付宝app,3:余额支付", required = true)
    @NotNull
    @Range(min = 0, max = 3)
    private Byte payType;
}
