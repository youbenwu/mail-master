package com.ys.mail.model.query;

import com.ys.mail.annotation.BlankOrPattern;
import com.ys.mail.enums.RegularEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 产品分类标签查询
 *
 * @author CRH
 * @date 2022-05-05 13:05
 * @since 1.0
 */
@Data
@ApiModel(value = "CgyTagProductQuery", description = "产品分类标签查询")
public class CgyTagProductQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "分类ID，可选")
    @BlankOrPattern(regEnum = RegularEnum.KEY)
    private String pdtCgyId;

    @ApiModelProperty(value = "会员标记，默认是首页数据，true->会员专享，可选")
    private Boolean member;

    @ApiModelProperty(value = "产品ID，用于分页，可选")
    @BlankOrPattern(regEnum = RegularEnum.KEY)
    private String productId;

    @ApiModelProperty(value = "会员特惠：0->false,1->true，可选")
    private Boolean isLiveStatus;

    @ApiModelProperty(value = "会员尾品：0->false,1->true，可选")
    private Boolean isHandpickStatus;

    @ApiModelProperty(value = "潮品专区：0->false,1->true，可选")
    private Boolean isDelicacyStatus;

    @ApiModelProperty(value = "甄选好物：0->false,1->true，可选")
    private Boolean isStyleStatus;

}
