package com.ys.mail.model.admin.vo.statistics;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Desc 订单汇总数据
 * @Author CRH
 * @Create 2022-03-02 10:32
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "PcOrderCollectDataVo")
public class PcOrderCollectDataVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单类型：0->正常订单；1->秒杀订单;2->拼团订单;3->礼品订单")
    private Integer orderType;

    @ApiModelProperty(value = "订单数")
    private Integer orderNumber;

    @ApiModelProperty(value = "订单金额，需要除100")
    private Long orderAmount;

    @ApiModelProperty(value = "订单数环比增长百分比，需要转换为百分比显示")
    private Double sequentialPercent = 0d;

}
