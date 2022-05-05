package com.ys.mail.model.param;

import com.ys.mail.annotation.BlankOrPattern;
import com.ys.mail.enums.RegularEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author ghdhj
 */
@Data
public class ProductParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品ID，用于分页")
    @BlankOrPattern(regEnum = RegularEnum.ID)
    private Long productId;

    @ApiModelProperty(value = "标记,默认是首页数据,true->表示会员专享数据")
    private Boolean flag;

    @ApiModelProperty(value = "商品分类ID，默认查全部")
    @BlankOrPattern(regEnum = RegularEnum.ID)
    private Long pdtCgyId;
}
