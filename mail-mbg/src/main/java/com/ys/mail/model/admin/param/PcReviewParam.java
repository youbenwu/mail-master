package com.ys.mail.model.admin.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;

/**
 * @Desc
 * @Author CRH
 * @Create 2021-12-29 21:30
 */
@Data
@ApiModel(value = "PcReviewParam", description = "提现审核参数")
public class PcReviewParam {

    @ApiModelProperty(value = "审核ID", required = true)
    @Pattern(regexp = "^\\d{19}$")
    private String reviewId;

    @ApiModelProperty(value = "审核状态：1->已通过，2->不通过，3->已关闭", required = true)
    @Pattern(regexp = "^[123]$")
    private String reviewState;

    @ApiModelProperty(value = "审核描述")
    private String reviewDescribe;

}
