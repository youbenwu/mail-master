package com.ys.mail.model.po;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author ghdhj
 */
@Data
public class PartnerProductPO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private Long partnerPdtId;

    @ApiModelProperty(value = "总金额")
    private Long totalPrice;

    @ApiModelProperty(value = "销量")
    private Integer sale;

    @ApiModelProperty(value = "详情")
    private String detailDesc;

    @ApiModelProperty(value = "画册图片，连产品图片限制为5张，以逗号分割")
    private String albumPics;

    @ApiModelProperty(value = "副标题")
    private String subTitle;

    @ApiModelProperty(value = "商品描述")
    private String description;

    @ApiModelProperty(value = "详情描述")
    private String detailTitle;

    @ApiModelProperty(value = "合伙人商品名称")
    private String partnerName;

    @ApiModelProperty(value = "服务描述")
    private String serveDesc;

    @ApiModelProperty(value = "案例图片")
    private String casePics;
}
