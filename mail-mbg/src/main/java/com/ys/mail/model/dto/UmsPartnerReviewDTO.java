package com.ys.mail.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author 24
 * @date 2022/1/19 9:06
 * @description 合伙人申请
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="UmsPartnerReview对象", description="合伙人申请")
public class UmsPartnerReviewDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "合伙人审核id")
    private Long partnerReviewId;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "身份证号")
    private String idyCard;

    @ApiModelProperty(value = "身份证正面照片")
    private String idyFront;

    @ApiModelProperty(value = "身份证反面照片")
    private String idyReverse;

    @ApiModelProperty(value = "公司名称")
    private String corporateName;

    @ApiModelProperty(value = "社会识别号")
    private String uniformSocialCreditCode;

    @ApiModelProperty(value = "职位")
    private String position;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "区")
    private String region;

    @ApiModelProperty(value = "详细地址")
    private String address;

}
