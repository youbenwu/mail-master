package com.ys.mail.model.admin.vo;

import com.ys.mail.entity.UmsIncome;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author ghdhj
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FreezeReMoneyVO extends UmsIncome implements Serializable {

    @ApiModelProperty(value = "待返还用户冻结收益对象")
    private UmsIncome umsIncome;
}
