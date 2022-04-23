package com.ys.mail.model.query;

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
 * @date 2022-01-01 16:05
 */
@Data
public class QueryQuickBuy implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品id",required = true)
    @NotBlank
    @Pattern(regexp = "^\\d{19}$")
    private String productId;

    @ApiModelProperty(value = "skuId",required = true)
    @NotBlank
    @Pattern(regexp = "^\\d{19}$")
    private String skuStockId;

    @ApiModelProperty(value = "数量默认1件,需求再增加时可以改",required = true)
    @NotNull
    @Range(min = 1,max = 1)
    private Byte quantity = 1;

    @ApiModelProperty(value = "限时购商品主键id",required = true)
    @NotBlank
    @Pattern(regexp = "^\\d{19}$")
    private String flashPromotionId;

    @ApiModelProperty(value = "商品秒杀价格")
    @NotNull
    private Long flashPromotionPrice;
}
