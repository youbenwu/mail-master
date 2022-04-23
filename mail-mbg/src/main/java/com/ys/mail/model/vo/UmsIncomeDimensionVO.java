package com.ys.mail.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 收益表
 * </p>
 *
 * @author 070
 * @since 2021-11-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "UmsIncomeDimensionVO对象", description = "收益历史记录维度表")
public class UmsIncomeDimensionVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "每笔交易ID，可用于分页")
    private String incomeId;

    @ApiModelProperty(value = "每笔交易的操作金额")
    private String income;

    @ApiModelProperty(value = "每笔交易的余额，统一存放")
    private String balance;

    @ApiModelProperty(value = "每笔交易的描述内容")
    private String detailSource;

    @ApiModelProperty(value = "每笔交易的日期时间")
    @JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "收益类型:-2->系统补还,-1->系统扣除,0->邀请收益,1->秒杀收益，2->余额提现，3->普通收益，" +
            "4->审核资金，5->审核退还，6->分佣收益，7->商家收益,8->创客收益,9->余额支付,10->扣除服务费,11->退还服务费")
    private Integer incomeType;

}
