package com.ys.mail.model.param.insert;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 商品店铺地址添加参数对象
 *
 * @author CRH
 * @date 2022-04-25 10:17
 * @since 1.0
 */
@Data
@ApiModel(value = "ProductStoreInsertParam", description = "商品店铺地址添加参数对象")
public class ProductStoreInsertParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank
    @Length(min = 1, max = 50)
    @ApiModelProperty(value = "店铺名称", required = true)
    private String storeName;

    @NotBlank
    @Length(min = 1, max = 10)
    @ApiModelProperty(value = "店主名称，用户名称", required = true)
    private String storeBoss;

    @NotBlank
    @Pattern(regexp = "^\\d{11}$")
    @ApiModelProperty(value = "店主手机，用户手机", required = true)
    private String storePhone;

    @NotBlank
    @Length(min = 1, max = 255)
    @ApiModelProperty(value = "店主经营地址", required = true)
    private String storeAddress;

}
