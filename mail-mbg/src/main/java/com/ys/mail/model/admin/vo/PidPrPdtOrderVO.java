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
public class PidPrPdtOrderVO extends PrPdtOrderVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "推荐人id")
    private Long parentId;

    @ApiModelProperty(value = "收益基本信息")
    private UmsIncome umsIncome;
}
