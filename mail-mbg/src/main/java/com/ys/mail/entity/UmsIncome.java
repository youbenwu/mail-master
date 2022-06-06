package com.ys.mail.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ys.mail.enums.IPairs;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

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
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "UmsIncome对象", description = "收益表")
public class UmsIncome implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "income_id", type = IdType.INPUT)
    private Long incomeId;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "收入=收益,1元就是100")
    private Long income = 0L;

    @ApiModelProperty(value = "支出=提现,1元就是100")
    private Long expenditure = 0L;

    @ApiModelProperty(value = "结余")
    private Long balance = 0L;

    @ApiModelProperty(value = "今日收益，1元就是100")
    private Long todayIncome = 0L;

    @ApiModelProperty(value = "总收益，不断累加")
    private Long allIncome = 0L;

    /**
     * 使用枚举 {@link IncomeType}
     * 补充说明：邀请、秒杀、分佣、普通收益：+ ，payType为3
     * 余额提现：-，payType为 2
     * 审核资金：-，审核退还：+，payType为 3
     * 系统扣除：-，系统补还：+，payType为 3
     */
    @ApiModelProperty(value = "收益类型:-2->系统补还,-1->系统扣除,0->邀请收益,1->秒杀收益，2->余额提现，3->普通收益，" +
            "4->审核资金，5->审核退还，6->分佣收益，7->商家收益,8->创客收益,9->余额支付,10->扣除服务费,11->退还服务费," +
            "12->邀请创客冻结收益,13->解冻邀请创客冻结收益,14->会员订单收益,15->用户退款秒杀产品")
    private Integer incomeType;

    /**
     * 使用枚举 {@link PayType}
     */
    @ApiModelProperty(value = "支付/收款方式：0->未支付，1->云闪付，2->支付宝，3->余额")
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
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "逻辑删除：0->未删除；1->删除")
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleted;

    @ApiModelProperty(value = "来源类型:0->平台回购,1->用户秒杀购买")
    private Byte originType;

    @ApiModelProperty(value = "秒杀商品主键id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long flashPromotionPdtId;

    @Getter
    @AllArgsConstructor
    @Accessors(fluent = true)
    public enum IncomeType implements IPairs<Integer, String, IncomeType> {
        /**
         * 状态
         */
        MINUS_TWO(-2, "系统补还"),
        MINUS_ONE(-1, "系统扣除"),
        ZERO(0, "邀请收益"),
        ONE(1, "秒杀收益"),
        TWO(2, "余额提现"),
        THREE(3, "普通收益"),
        FOUR(4, "审核资金"),
        FIVE(5, "审核退还"),
        SIX(6, "团长分佣"),
        SEVEN(7, "商家收益"),
        EIGHT(8, "创客收益"),
        NINE(9, "余额支付"),
        TEN(10, "扣除服务费"),
        ELEVEN(11, "退还服务费"),
        TWELVE(12,"邀请创客冻结收益"),
        THIRTEEN(13,"解冻邀请创客冻结收益"),
        FOURTEEN(14,"会员订单收益"),
        FIFTEEN(15,"用户退款秒杀产品")
        ;
        final Integer key;
        final String value;
    }

    @Getter
    @AllArgsConstructor
    @Accessors(fluent = true)
    public enum PayType implements IPairs<Integer, String, PayType> {
        /**
         * 状态
         */
        ZERO(0, "未支付"),
        ONE(1, "云闪付"),
        TWO(2, "支付宝"),
        THREE(3, "余额"),
        ;
        final Integer key;
        final String value;
    }
}
