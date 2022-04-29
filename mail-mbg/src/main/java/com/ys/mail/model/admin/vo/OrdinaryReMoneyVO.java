package com.ys.mail.model.admin.vo;

import com.ys.mail.entity.UmsIncome;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author ghdhj
 */
@Data
public class OrdinaryReMoneyVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "总金额")
    private Long totalAmount;

    @ApiModelProperty(value = "数量")
    private Integer quantity;

    @ApiModelProperty(value = "上级id")
    private Long parentId;

    @ApiModelProperty(value = "收益对象")
    private UmsIncome umsIncome;
}
