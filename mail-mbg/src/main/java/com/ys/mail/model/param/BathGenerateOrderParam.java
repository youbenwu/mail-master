package com.ys.mail.model.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.List;

/**
 * 批量购买商品（清空购物车）
 */
@Data
public class BathGenerateOrderParam implements Serializable {

    @ApiModelProperty(value = "收货人姓名",required = true)
    @NotBlank
    private String receiverName;

    @ApiModelProperty(value = "收货人电话",required = true)
    @NotBlank
    @Pattern(regexp = "^[1][3,4,5,6,7,8,9][0-9]{9}$")
    private String receiverPhone;

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

    @ApiModelProperty(value = "购物车id集合",required = true)
    @NotEmpty(message = "请选择商品")
    private List<Long> omsCartItemIds;

    @ApiModelProperty(value = "0代表是线上发布1代表是线上发货",required = true)
    @NotNull
    @Range(min = 0,max = 1)
    private Integer orderSelect;

    @ApiModelProperty(value = "订单备注")
    @Size(max = 36)
    private String orderNote;

    @ApiModelProperty(value = "应付金额（实际支付金额）")
    private Long payAmount;
}
