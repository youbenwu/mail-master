package com.ys.mail.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 24
 * @date 2022/1/22 11:31
 * @description 合伙人列表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="UmsPartnerVO对象", description="")
public class UmsPartnerVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "合伙人id")
    private Long partnerId;

    @ApiModelProperty(value = "用户id")
    private Long userId;

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

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "创建时间")
    private Date updateTime;


}
