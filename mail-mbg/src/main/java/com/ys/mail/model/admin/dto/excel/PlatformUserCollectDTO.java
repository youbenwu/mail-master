package com.ys.mail.model.admin.dto.excel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ys.mail.entity.UmsUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Desc 平台用户汇总数据
 * @Author CRH
 * @Create 2022-03-30 16:44
 */
@Data
@ApiModel(value = "PlatformUserCollectDTO")
public class PlatformUserCollectDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    @ApiModelProperty(value = "个人基本信息")
    private UmsUser umsUser;

    @ApiModelProperty(value = "个人收益汇总数据")
    private IncomeCollectDTO incomeCollectDTO;

    @ApiModelProperty(value = "个人最新余额数据")
    private UserBalanceDTO userBalanceDTO;

    @ApiModelProperty(value = "个人订单汇总数据")
    private OrderCollectDTO orderCollectDTO;

    @ApiModelProperty(value = "个人审核汇总数据")
    private ReviewCollectDTO reviewCollectDTO;

    @ApiModelProperty(value = "个人秒杀汇总数据")
    private SecKillCollectDTO secKillCollectDTO;

}
