package com.ys.mail.model.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;

/**
 * 购物车下单参数对象,臃肿不好修改,暂时先这样
 * @author ghdhj
 */
@Data
public class CreateOrderParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "收货人姓名",required = true)
    @NotBlank
    private String receiverName;

    @ApiModelProperty(value = "收货人电话",required = true)
    @NotBlank
    @Pattern(regexp = "^[1][3456789][0-9]{9}$")
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

    @ApiModelProperty(value = "前端计算好的总价格",required = true)
    @NotNull
    @Range(min = 1,message = "请输入正确的金额")
    private Long totalPrice;

    @ApiModelProperty(value = "集合商品")
    private List<CartProduct> carts;

    @Data
    private static class CartProduct{

        @ApiModelProperty(value = "skuId",required = true)
        private Long skuId;

        @ApiModelProperty(value = "留言,可以不填")
        private String orderNote;

    }

}
