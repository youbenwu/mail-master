package com.ys.mail.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ys.mail.annotation.EnumDocumentValid;
import com.ys.mail.entity.UmsIncome;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用一句简单的话来描述下该类
 *
 * @author CRH
 * @date 2022-06-14 11:08
 * @since 1.0
 */
@Data
@ApiModel(value = "UmsIncomeVO")
public class UmsIncomeVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long incomeId;

    @ApiModelProperty(value = "收入=收益")
    private Double income;

    @ApiModelProperty(value = "支出=提现")
    private Double expenditure;

    @ApiModelProperty(value = "原本金，货款")
    private Double original;

    @ApiModelProperty(value = "收益，积分")
    private Double integral;

    @ApiModelProperty(value = "结余")
    private Double balance;

    @ApiModelProperty(value = "今日收益")
    private Double todayIncome;

    @ApiModelProperty(value = "总收益，不断累加")
    private Double allIncome;

    @ApiModelProperty(value = "收益类型")
    @EnumDocumentValid(enumClass = UmsIncome.IncomeType.class, isValid = false)
    private Integer incomeType;

    @ApiModelProperty(value = "支付/收款方式")
    @EnumDocumentValid(enumClass = UmsIncome.PayType.class, isValid = false)
    private Integer payType;

    @ApiModelProperty(value = "来源,支付成功回调后拼接字符串保存")
    private String detailSource;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "支付宝订单号")
    private String incomeNo;

    @ApiModelProperty(value = "商户订单号自己用")
    private String orderTradeNo;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "来源类型:0->平台回购,1->用户秒杀购买")
    private Byte originType;

    @ApiModelProperty(value = "秒杀商品主键id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long flashPromotionPdtId;

}
