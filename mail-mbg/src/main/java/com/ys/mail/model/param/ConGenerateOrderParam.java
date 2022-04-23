package com.ys.mail.model.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author ghdhj
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ConGenerateOrderParam extends GenerateOrderParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "是否使用会员价,默认为null,false代表不使用,true代表使用")
    private Boolean flag;

    @ApiModelProperty(value = "会员价,一元就是一百")
    private Long mebPrice;
}
