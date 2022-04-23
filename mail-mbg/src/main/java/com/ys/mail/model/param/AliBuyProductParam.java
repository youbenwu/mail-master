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
 * @date 2021-12-20 11:10
 */
@Data
public class AliBuyProductParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单编号",required = true)
    @Pattern(regexp = "^\\d+$")
    @NotBlank
    private String orderSn;

    @ApiModelProperty(value = "支付金额",required = true)
    @Pattern(regexp = "^\\d+$")
    @NotBlank
    private String amount;

    @ApiModelProperty(value = "公司类型,记录由那个公司收款,0->大尾狐,1->呼啦兔",required = true)
    //@NotNull
    @Range(min = 0,max = 1)
    private Byte cpyType;
}
