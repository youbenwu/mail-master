package com.ys.mail.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author DT
 * @version 1.0
 * @date 2021-11-29 13:26
 */
@Data
@ApiModel(value = "GenerateOrderParam", description = "下单参数对象")
public class GenerateOrderParam implements Serializable {

    @ApiModelProperty(value = "收货人姓名",required = true)
    @NotBlank
    private String receiverName;

    @ApiModelProperty(value = "收货人电话",required = true)
    @NotBlank
    @Pattern(regexp = "^1[3-9]\\d{9}$")
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

    @ApiModelProperty(value = "主图",required = true)
    @NotBlank
    private String pic;

    @ApiModelProperty(value = "商品id",required = true)
    @NotBlank
    @Pattern(regexp = "^\\d{19}$")
    private String productId;

    @ApiModelProperty(value = "商品分类id",required = true)
    @NotBlank
    @Pattern(regexp = "^\\d{19}$")
    private String pdtCgyId;

    @ApiModelProperty(value = "商品名称",required = true)
    @NotBlank
    private String productName;

    @ApiModelProperty(value = "sku_id",required = true)
    @NotBlank
    @Pattern(regexp = "^\\d{19}$")
    private String skuStockId;

    @ApiModelProperty(value = "数量",required = true)
    @NotNull
    private Integer quantity;

    @ApiModelProperty(value = "商品销售属性",required = true)
    @NotBlank
    private String spData;

    @ApiModelProperty(value = "商品价格,1:100,1元就是代表100,普通价格",required = true)
    @NotNull
    private Long price;

    @ApiModelProperty(value = "0代表是线上发布1代表是线上发货",required = true)
    @NotNull
    @Range(min = 0,max = 1)
    private Integer orderSelect;

    @ApiModelProperty(value = "限时购id,秒杀必传,普通可以不用传")
    private Long flashPromotionId;

    @ApiModelProperty(value = "订单备注")
    @Size(max = 36)
    private String orderNote;

    @ApiModelProperty(value = "0->大尾狐,1->呼啦兔")
    @NotNull
    @Range(min = 0,max = 1)
    private Byte cpyType;
}
