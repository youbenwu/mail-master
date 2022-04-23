package com.ys.mail.model.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.ys.mail.entity.UmsPartnerReview;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author 070
 * @since 2022-01-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="UmsPartnerReviewVO对象", description="")
public class UmsPartnerReviewVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "合伙人审核id")
    private Long partnerReviewId;

    @ApiModelProperty(value = "申请用户id")
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

    @ApiModelProperty(value = "认证状态:0审核失败1审核成功2待审核")
    private Integer authStatus;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    public UmsPartnerReviewVO(UmsPartnerReview review) {
        BeanUtils.copyProperties(review,this);
    }
}
