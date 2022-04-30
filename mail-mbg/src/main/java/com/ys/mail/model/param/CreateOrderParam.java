package com.ys.mail.model.param;

import com.ys.mail.annotation.BlankOrPattern;
import com.ys.mail.enums.SettingTypeEnum;
import com.ys.mail.exception.ApiAssert;
import com.ys.mail.exception.code.BusinessErrorCode;
import com.ys.mail.util.BlankUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.math.NumberUtils;
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

    @ApiModelProperty(value = "收货地址id",required = true)
    @BlankOrPattern(regexp = "^\\d{19}$")
    @NotNull(message = "请输入地址")
    private Long addressId;

    @ApiModelProperty(value = "购物车集合对象")
    @NotEmpty
    private List<Cart> carts;

    @ApiModelProperty(value = "留言")
    private String orderNote;

    @ApiModelProperty(value = "总金额",required = true)
    @Range(min = 1,message = "总金额最小值不能小于1分")
    private Long totalAmount;

    public static class Cart{

        @ApiModelProperty(value = "集合skuId",required = true)
        private Long skuId;

        @ApiModelProperty(value = "数量,可以为null")
        private Integer num;

        public Long getSkuId() {
            return skuId;
        }

        public void setSkuId(Long skuId) {
            ApiAssert.noValue(skuId, BusinessErrorCode.SKU_ID_NULL);
            if(skuId.toString().length() != SettingTypeEnum.nineteen.key()){
                ApiAssert.fail(BusinessErrorCode.ERR_SKU_ID);
            }
            this.skuId = skuId;
        }

        public Integer getNum() {
            return num;
        }

        public void setNum(Integer num) {
            this.num = BlankUtil.isEmpty(num) ? NumberUtils.INTEGER_ONE :num;
        }
    }
}
