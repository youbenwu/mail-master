package com.ys.mail.model.admin.vo.statistics;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Desc 每月汇总数据（订单、流水）
 * @Author CRH
 * @Create 2022-03-07 09:41
 */
@Data
@ApiModel(value = "EveryMonthCollectDataVo",description = "每月汇总数据（订单、流水）")
public class EveryMonthCollectDataVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "每天日期")
    private String day;

    @ApiModelProperty(value = "每日数量")
    private Integer count;

    @ApiModelProperty(value = "每日金额")
    private Long money;

}
