package com.ys.mail.model.admin.dto.excel;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Desc 秒杀汇总数据
 * @Author CRH
 * @Create 2022-03-30 16:44
 */
@Data
@ApiModel(value = "SecKillCollectDTO")
public class SecKillCollectDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    @ApiModelProperty(value = "待秒杀")
    private Double waitSecKill;

    @ApiModelProperty(value = "已上架")
    private Double waitPutAway;

    @ApiModelProperty(value = "待卖出")
    private Double waitSell;

    @ApiModelProperty(value = "总未卖")
    private Double sumSell;

    @ApiModelProperty(value = "预计卖出收益")
    private Double sumIncome;

}
