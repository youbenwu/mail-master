package com.ys.mail.model.vo;

import com.ys.mail.entity.UmsIncome;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ghdhj
 */
@Data
public class PartnerReIncomeVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "累积总金额")
    private Long periodsPrice;

    @ApiModelProperty(value = "总数量")
    private Integer reNum;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "状态:0->未退还,1->退还成功,2->退还失败")
    private Byte periodsStatus;

    @ApiModelProperty(value = "退还时间,yyyy-MM-dd,每月1号")
    private Date periodsDate;

    @ApiModelProperty(value = "收益对象")
    private UmsIncome income;
}
