package com.ys.mail.model.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author DT
 * @version 1.0
 * @date 2022-01-04 16:15
 */
@Data
public class QuickBuyProductQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品id")
    @NotBlank
    @Pattern(regexp = "^\\d{19}$")
    private String productId;

    @ApiModelProperty(value = "场次id")
    @NotBlank
    @Pattern(regexp = "^\\d{19}$")
    private String flashPromotionId;

    @ApiModelProperty(value = "商品秒杀价格", required = true)
    @NotNull
    private Long flashPromotionPrice;
}
