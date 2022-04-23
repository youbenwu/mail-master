package com.ys.mail.model.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author DT
 * @version 1.0
 * @date 2021-11-17 13:59
 */
@Data
public class IntegralConvertParam implements Serializable {

    @ApiModelProperty(value = "收货人姓名",required = true)
    @NotBlank
    private String receiverName;

    @ApiModelProperty(value = "收货人电话",required = true)
    @NotBlank
    @Pattern(regexp = "^[1][3,4,5,6,7,8,9][0-9]{9}$")
    private String receiverPhone;

    @ApiModelProperty(value = "收货人邮编")
    private String receiverCode;

    @ApiModelProperty(value = "省份/直辖市",required = true)
    @NotBlank
    private String receiverProvince;

    @ApiModelProperty(value = "城市",required = true)
    @NotBlank
    private String receiverCity;

    @ApiModelProperty(value = "区",required = true)
    @NotBlank
    private String receiverRegion;

    @ApiModelProperty(value = "详细地址",required = true)
    @NotBlank
    private String receiverAddress;

    @ApiModelProperty(value = "订单备注")
    private String orderNote;

    @ApiModelProperty(value = "积分商品id",required = true)
    @NotBlank
    @Pattern(regexp = "^\\d{19}$")
    private String integralPdtId;

    @ApiModelProperty(value = "数量,商品的数量",required = true)
    @NotNull
    @Min(value = 1)
    private Integer quantity;

    @ApiModelProperty(value = "使用总积分数",required = true)
    @NotNull
    private Long totalAmount;
}
