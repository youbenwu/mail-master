package com.ys.mail.model.admin.vo;

import com.ys.mail.entity.PcReview;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Desc
 * @Author CRH
 * @Create 2021-12-29 20:01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PcReviewVO extends PcReview implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "审核人名称")
    private String username;

    @ApiModelProperty(value = "申请人昵称")
    private String nickname;
}
