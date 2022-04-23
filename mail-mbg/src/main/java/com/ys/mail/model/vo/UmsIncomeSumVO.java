package com.ys.mail.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Desc 收益汇总对象
 * @Author CRH
 * @Create 2021-12-07 19:26
 * @Email 18218292802@163.com
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "UmsIncomeVO对象", description = "收益统计表")
public class UmsIncomeSumVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "余额，结余")
    private String balance = "0";

    @ApiModelProperty(value = "今日收益 --> 快购总收益")
    private String todayIncome = "0";

    @ApiModelProperty(value = "总收益 --> 当月总收益")
    private String allIncome = "0";

    @ApiModelProperty(value = "邀请收益汇总，社团奖励")
    private String inviteIncomeSum;

    @ApiModelProperty(value = "秒杀收益汇总，秒杀收益")
    private String saleIncomeSum;

    @ApiModelProperty(value = "提现记录汇总，提现支出")
    private String expenditureSum;

    @ApiModelProperty(value = "普通收益汇总，佣金奖励")
    private String generalIncomeSum;

}
