package com.ys.mail.model.admin.dto.excel;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Desc 订单汇总数据
 * @Author CRH
 * @Create 2022-03-30 16:44
 */
@Data
@ApiModel(value = "OrderCollectDTO")
public class OrderCollectDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    @ApiModelProperty(value = "下单支付账号列表")
    private String payList;

    @ApiModelProperty(value = "平台总消费")
    private Double sumConsume;

    @ApiModelProperty(value = "最近支付时间")
    private Date latestPaymentTime;

    @ApiModelProperty(value = "普通商品消费")
    private Double generalConsume;

    @ApiModelProperty(value = "秒杀商品消费")
    private Double secKillConsume;

    @ApiModelProperty(value = "会员商品消费")
    private Double memberConsume;

    @ApiModelProperty(value = "创客商品消费")
    private Double makerConsume;

    @ApiModelProperty(value = "已退款")
    private Double refunded;

    @ApiModelProperty(value = "未退款")
    private Double notRefund;

    @ApiModelProperty(value = "待收货")
    private Double waitTake;

}
