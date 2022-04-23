package com.ys.mail.model.admin.query;

import com.ys.mail.annotation.BlankOrPattern;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Desc 订单查询对象
 * @Author CRH
 * @Create 2021-12-27 13:48
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OmsOrderQuery extends Query {

    @ApiModelProperty(value = "用户ID，支持任意模糊匹配")
    @BlankOrPattern(regexp = "^\\d{19}$", message = "用户ID不合法，请检查！")
    private String userId;

    @ApiModelProperty(value = "订单编号，支持任意模糊匹配")
    @BlankOrPattern(regexp = "^\\d+$", message = "订单编号不合法，请检查！")
    private String orderSn;

    @ApiModelProperty(value = "物流公司，支持任意模糊匹配")
    private String deliveryCompany;

    @ApiModelProperty(value = "物流单号，支持任意模糊匹配")
    @BlankOrPattern(regexp = "^\\d$", message = "物流单号不合法，请检查！")
    private String deliverySn;

    @ApiModelProperty(value = "第三方交易流水号,例如云闪付,支付宝等，支持任意模糊匹配")
    @BlankOrPattern(regexp = "^\\d$", message = "第三方交易流水号不合法，请检查！")
    private String transId;

    @ApiModelProperty(value = "支付方式：0->未支付；1云闪付，2支付宝")
    @BlankOrPattern(regexp = "[0-2]", message = "支付方式不合法，请检查！")
    private String payType;

    @ApiModelProperty(value = "订单来源：0->PC订单；1->app订单")
    @BlankOrPattern(regexp = "[01]", message = "订单来源不合法，请检查！")
    private String sourceType;

    @ApiModelProperty(value = "订单状态：0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单；6->已付款；7->待评价")
    @BlankOrPattern(regexp = "[0-7]", message = "订单状态不合法，请检查！")
    private String orderStatus;

    @ApiModelProperty(value = "订单类型：0->正常订单；3->礼品订单")
    @BlankOrPattern(regexp = "[03]", message = "订单类型不合法，请检查！")
    private String orderType;

    @ApiModelProperty(value = "发票类型：0->不开发票；1->电子发票；2->纸质发票")
    @BlankOrPattern(regexp = "[0-2]", message = "发票类型不合法，请检查！")
    private String billType;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "手机号")
    private String phone;

}


