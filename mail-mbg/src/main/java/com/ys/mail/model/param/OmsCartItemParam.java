package com.ys.mail.model.param;

import com.ys.mail.annotation.BlankOrPattern;
import com.ys.mail.entity.OmsCartItem;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.IdWorker;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.math.NumberUtils;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author DT
 * @version 1.0
 * @date 2021-12-30 10:43
 */
@Data
public class OmsCartItemParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品id",required = true)
    @NotNull
    @BlankOrPattern(regexp = "^\\d{19}$",message = "请输入正确的id")
    private Long productId;

    @ApiModelProperty(value = "商品数量,默认为1,可以不传,不传为null后台默认就是1件")
    private Integer quantity;

    @ApiModelProperty(value = "商品skuId",required = true)
    @NotNull
    @BlankOrPattern(regexp = "^\\d{19}$",message = "请输入正确的id")
    private Long productSkuId;

    public void setQuantity(Integer quantity) {
        this.quantity = BlankUtil.isEmpty(quantity) ? NumberUtils.INTEGER_ONE : quantity;
    }

    public OmsCartItem getParam(Long userId){
        return OmsCartItem.builder()
                .cartItemId(IdWorker.generateId())
                .userId(userId)
                .productId(productId)
                .productSkuId(productSkuId)
                .quantity(quantity)
                .build();
    }
}
