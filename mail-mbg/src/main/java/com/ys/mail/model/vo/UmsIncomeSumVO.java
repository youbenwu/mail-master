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

    @ApiModelProperty(value = "总收益")
    private String allIncome;

    @ApiModelProperty(value = "货款")
    private String original;

    @ApiModelProperty(value = "积分")
    private String integral;

    @ApiModelProperty(value = "手续费")
    private String rate;

    @ApiModelProperty(value = "余额")
    private String balance;

}
