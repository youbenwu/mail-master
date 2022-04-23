package com.ys.mail.model.admin.vo.statistics;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Desc 审核汇总数据
 * @Author CRH
 * @Create 2022-02-21 15:19
 */
@Data
@ApiModel(value = "PcReviewCollectDataVo", description = "提现审核数据统计显示对象")
public class PcReviewCollectDataVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "申请总人数")
    private Integer totalNumber;

    @ApiModelProperty(value = "申请总金额")
    private Double totalAmount;

    @ApiModelProperty(value = "已失效人数")
    private Integer expiredNumber;

    @ApiModelProperty(value = "待审核人数")
    private Integer unReviewNumber;

    @ApiModelProperty(value = "已通过人数")
    private Integer passedNumber;

    @ApiModelProperty(value = "已驳回人数")
    private Integer refusedNumber;

    @ApiModelProperty(value = "已关闭人数")
    private Integer closedNumber;

    @ApiModelProperty(value = "已关闭人数")
    private Integer cancelNumber;

    @ApiModelProperty(value = "已失效金额")
    private Double expiredAmount;

    @ApiModelProperty(value = "待审核金额")
    private Double unReviewAmount;

    @ApiModelProperty(value = "已通过金额")
    private Double passedAmount;

    @ApiModelProperty(value = "已驳回金额")
    private Double refusedAmount;

    @ApiModelProperty(value = "已关闭金额")
    private Double closedAmount;

    @ApiModelProperty(value = "已关闭金额")
    private Double cancelAmount;

}
