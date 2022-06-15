package com.ys.mail.model.vo;

import com.ys.mail.constant.StringConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 收益汇总对象
 *
 * @author CRH
 * @date 2022-04-19 15:19
 * @since 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "UmsIncomeVO对象", description = "收益统计表")
public class UmsIncomeSumVO implements Serializable {

    private static final long serialVersionUID = 1L;

    public UmsIncomeSumVO() {
        this.allIncome = StringConstant.ZERO;
        this.original = StringConstant.ZERO;
        this.integral = StringConstant.ZERO;
        this.rate = StringConstant.ZERO;
        this.balance = StringConstant.ZERO;
    }

    @ApiModelProperty(value = "总收益（主要显示），单位：元")
    private String allIncome;

    @ApiModelProperty(value = "货款（主要显示），单位：元")
    private String original;

    @ApiModelProperty(value = "积分（底部显示），单位：个")
    private String integral;

    @ApiModelProperty(value = "服务费（底部显示），单位：元")
    private String rate;

    @ApiModelProperty(value = "余额（隐藏计算），单位：元")
    private String balance;

    @ApiModelProperty(value = "（旧）今日收益 --> 快购总收益")
    private String todayIncome;

    @ApiModelProperty(value = "（旧）邀请收益汇总，社团奖励")
    private String inviteIncomeSum;

    @ApiModelProperty(value = "（旧）秒杀收益汇总，秒杀收益")
    private String saleIncomeSum;

    @ApiModelProperty(value = "（旧）提现记录汇总，提现支出")
    private String expenditureSum;

    @ApiModelProperty(value = "（旧）普通收益汇总，佣金奖励")
    private String generalIncomeSum;

}
