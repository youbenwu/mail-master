package com.ys.mail.model.admin.dto.excel;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Desc 收益汇总数据
 * @Author CRH
 * @Create 2022-03-30 16:44
 */
@Data
@ApiModel(value = "IncomeCollectDTO")
public class IncomeCollectDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    @ApiModelProperty(value = "秒杀流水")
    private Double secKillIncome;

    @ApiModelProperty(value = "余额提现")
    private Double balanceEx;

    @ApiModelProperty(value = "审核资金")
    private Double reviewDeduct;

    @ApiModelProperty(value = "审核退还")
    private Double reviewRefund;

    @ApiModelProperty(value = "分佣收益")
    private Double teamIncome;

    @ApiModelProperty(value = "商家收益")
    private Double merchantIncome;

    @ApiModelProperty(value = "创客收益")
    private Double makerIncome;

    @ApiModelProperty(value = "余额支付")
    private Double balancePay;

    @ApiModelProperty(value = "扣除服务费")
    private Double serviceChargeDeduct;

    @ApiModelProperty(value = "退还服务费")
    private Double serviceChargeRefund;

    @ApiModelProperty(value = "当天小额提现，<=1000")
    private Double smallAmount;
}
