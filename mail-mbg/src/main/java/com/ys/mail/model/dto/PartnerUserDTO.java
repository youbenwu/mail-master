package com.ys.mail.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用一句简单的话来描述下该类
 *
 * @author CRH
 * @date 2022-04-30 14:11
 * @since 1.0
 */
@Data
public class PartnerUserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品收藏id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long partnerId;

    @ApiModelProperty(value = "公司名称")
    private String corporateName;

    @ApiModelProperty(value = "公司名称")
    private String phone;

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "区")
    private String region;

    @ApiModelProperty(value = "详细地址")
    private String address;

    @ApiModelProperty(value = "头像")
    private String headPortrait;

    @ApiModelProperty(value = "距离，单位m")
    private Double distance;
}
