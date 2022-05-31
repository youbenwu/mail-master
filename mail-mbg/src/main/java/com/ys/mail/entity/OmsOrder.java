package com.ys.mail.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * 订单表
 * </p>
 *
 * @author 070
 * @since 2021-11-23
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "OmsOrder对象", description = "订单表")
public class OmsOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单id")
    @TableId(value = "order_id", type = IdType.INPUT)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long orderId;

    @ApiModelProperty(value = "用户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    @ApiModelProperty(value = "优惠卷id")
    private Long couponId;

    @ApiModelProperty(value = "订单编号")
    private String orderSn;

    @ApiModelProperty(value = "订单总金额")
    private Long totalAmount;

    @ApiModelProperty(value = "应付金额（实际支付金额）")
    private Long payAmount;

    @ApiModelProperty(value = "运费金额,默认为0,也可以有")
    private Long freightAmount;

    @ApiModelProperty(value = "促销优化金额（促销价、满减、阶梯价）")
    private Long promotionAmount;

    @ApiModelProperty(value = "优惠券抵扣金额")
    private Long couponAmount;

    @ApiModelProperty(value = "管理员后台调整订单使用的折扣金额")
    private Long discountAmount;

    /**
     * 使用枚举 {@link PayType}
     */
    @ApiModelProperty(value = "支付方式：0->未支付;1->云闪付;2->支付宝;3->余额支付")
    private Integer payType;

    @ApiModelProperty(value = "订单来源：0->PC订单；1->app订单")
    private Integer sourceType;

    /**
     * 使用枚举 {@link OrderStatus}
     */
    @ApiModelProperty(value = "订单状态：0->待付款；1->待发货；2->已发货；3->已完成；" +
            "4->已关闭；5->无效订单；6->已付款；7->待评价; 8->已核销")
    private Integer orderStatus;

    /**
     * 使用枚举 {@link OrderType}
     */
    @ApiModelProperty(value = "订单类型：0->正常订单；1->秒杀订单;2->拼团订单;3->付费会员订单;4->创客订单;5->会员订单")
    private Integer orderType;

    @ApiModelProperty(value = "物流公司(配送方式)")
    private String deliveryCompany;

    @ApiModelProperty(value = "物流单号")
    private String deliverySn;

    @ApiModelProperty(value = "自动确认时间（天）")
    private Integer autoConfirmDay;

    @ApiModelProperty(value = "可以获得的积分")
    private Integer integration;

    @ApiModelProperty(value = "活动信息")
    private String promotionInfo;

    @ApiModelProperty(value = "发票类型：0->不开发票；1->电子发票；2->纸质发票")
    private Integer billType;

    @ApiModelProperty(value = "发票抬头")
    private String billHeader;

    @ApiModelProperty(value = "发票内容")
    private String billContent;

    @ApiModelProperty(value = "收票人电话")
    private String billPhone;

    @ApiModelProperty(value = "收票人邮箱")
    private String billEmail;

    @ApiModelProperty(value = "收货人姓名")
    private String receiverName;

    @ApiModelProperty(value = "收货人电话")
    private String receiverPhone;

    @ApiModelProperty(value = "收货人邮编")
    private String receiverCode;

    @ApiModelProperty(value = "省份/直辖市")
    private String receiverProvince;

    @ApiModelProperty(value = "城市")
    private String receiverCity;

    @ApiModelProperty(value = "区")
    private String receiverRegion;

    @ApiModelProperty(value = "详细地址")
    private String receiverAddress;

    @ApiModelProperty(value = "订单备注")
    private String orderNote;

    @ApiModelProperty(value = "确认收货状态：0->未确认；1->已确认")
    private Integer isConfirmStatus;

    @ApiModelProperty(value = "删除状态：0->未删除；1->已删除")
    private Integer deleteStatus;

    @ApiModelProperty(value = "支付时间")
    private Date paymentTime;

    @ApiModelProperty(value = "发货时间")
    private Date deliveryTime;

    @ApiModelProperty(value = "确认收货时间")
    private Date receiveTime;

    @ApiModelProperty(value = "评价时间")
    private Date commentTime;

    @ApiModelProperty(value = "提交时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "0代表是线上发布1代表是线上发货")
    private Integer orderSelect;

    @ApiModelProperty(value = "是否评价,商品在已完成收货后评价就是1，未评价就是0")
    @TableField("is_appraise")
    private Boolean appraise;

    @ApiModelProperty(value = "第三方交易流水号,例如云闪付,支付宝等")
    private String transId;

    @ApiModelProperty(value = "公司类型订单:0->大尾狐,1->呼啦兔")
    private Byte cpyType;

    @ApiModelProperty(value = "合伙人id")
    private Long partnerId;

    @ApiModelProperty(value = "付款用户号(支付宝,微信等)")
    private String buyerLogonId;

    @ApiModelProperty(value = "合伙人价格")
    private Long partnerPrice;

    @ApiModelProperty(value = "截止时间，作用于合伙人商品，过期则不能上架、核销等")
    private Date expireTime;

    @Getter
    @AllArgsConstructor
    @Accessors(fluent = true)
    public enum PayType implements IPairs<Integer, String, PayType> {
        /**
         * 支付类型
         */
        ZERO(0, "未支付"),
        ONE(1, "云闪付"),
        TWO(2, "支付宝"),
        THREE(3, "余额支付"),
        ;
        final Integer key;
        final String value;
        public static final String DOCUMENT = "支付状态：0->未支付，1->云闪付，2->支付宝，3->余额支付";
    }

    @Getter
    @AllArgsConstructor
    @Accessors(fluent = true)
    public enum OrderStatus implements IPairs<Integer, String, OrderStatus> {
        /**
         * 订单状态
         */
        ZERO(0, "待付款"),
        ONE(1, "待发货"),
        TWO(2, "已发货"),
        THREE(3, "已完成"),
        FOUR(4, "已关闭"),
        FIVE(5, "无效订单"),
        SIX(6, "已付款"),
        SEVEN(7, "待评价"),
        EIGHT(8, "已核销"),
        ;
        final Integer key;
        final String value;
        public static final String DOCUMENT = "订单状态：0->待付款，1->待发货，2->已发货，3->已完成，4->已关闭，5->无效订单，6->已付款，7->待评价，8->已核销";
    }

    @Getter
    @AllArgsConstructor
    @Accessors(fluent = true)
    public enum OrderType implements IPairs<Integer, String, OrderType> {
        /**
         * 订单类型
         */
        ZERO(0, "正常订单"),
        ONE(1, "秒杀订单"),
        TWO(2, "拼团订单"),
        THREE(3, "付费会员订单"),
        FOUR(4, "创客订单"),
        FIVE(5, "会员订单"),
        ;
        final Integer key;
        final String value;
        public static final String DOCUMENT = "订单类型：0->正常订单，1->秒杀订单，2->拼团订单，3->付费会员订单，4->创客订单，5->会员订单";
    }

}
