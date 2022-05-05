package com.ys.mail.model.query;

import com.ys.mail.annotation.BlankOrPattern;
import com.ys.mail.enums.RegularEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 产品分类查询
 *
 * @author CRH
 * @date 2022-05-05 13:05
 * @since 1.0
 */
@Data
@ApiModel(value = "CgyProductQuery", description = "产品分类查询")
public class CgyProductQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank
    @ApiModelProperty(value = "父级导航分类ID，必传", required = true)
    @BlankOrPattern(regEnum = RegularEnum.KEY)
    private String parentId;

    @ApiModelProperty(value = "下级分类ID，可选")
    @BlankOrPattern(regEnum = RegularEnum.KEY)
    private String pdtCgyId;

    @ApiModelProperty(value = "会员标记,默认是首页数据,true->表示会员专享数据")
    private Boolean member;

}
