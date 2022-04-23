package com.ys.mail.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 收益表
 * </p>
 *
 * @author 070
 * @since 2021-11-25
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "UmsCalcIncomeVO对象", description = "收益计算对象")
public class UmsCalcIncomeVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "系统收益")
    private BigDecimal sysIncome;

    @ApiModelProperty(value = "商家收益")
    private BigDecimal merchantIncome;

    @ApiModelProperty(value = "新价格，用于发布商品使用")
    private BigDecimal totalIncome;

}
