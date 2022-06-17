package com.ys.mail.model.admin.query;

import com.ys.mail.annotation.BlankOrPattern;
import com.ys.mail.annotation.EnumDocumentValid;
import com.ys.mail.entity.OmsOrder;
import com.ys.mail.enums.RegularEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 订单查询对象
 *
 * @author CRH
 * @date 2022-04-19 15:19
 * @since 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OmsOrderQuery extends Query {

    @ApiModelProperty(value = "用户ID，支持右模糊匹配")
    @BlankOrPattern(regEnum = RegularEnum.KEY, message = "(用户编号)：不合法，请检查！")
    private String userId;

    @ApiModelProperty(value = "订单编号，支持任意模糊匹配")
    @BlankOrPattern(regEnum = RegularEnum.KEY, message = "(订单编号)：不合法，请检查！")
    private String orderSn;

    @ApiModelProperty(value = "物流公司，支持任意模糊匹配")
    private String deliveryCompany;

    @ApiModelProperty(value = "物流单号，支持任意模糊匹配")
    @BlankOrPattern(regEnum = RegularEnum.KEY, message = "(物流单号)：不合法，请检查！")
    private String deliverySn;

    @ApiModelProperty(value = "第三方交易流水号,例如云闪付,支付宝等，支持任意模糊匹配")
    @BlankOrPattern(regEnum = RegularEnum.KEY, message = "(交易流水号)：不合法，请检查！")
    private String transId;

    @ApiModelProperty(value = "订单来源：0->PC订单；1->app订单")
    @BlankOrPattern(regEnum = RegularEnum.ZERO_ONE, message = "(订单来源)：不合法，请检查！")
    private String sourceType;

    @ApiModelProperty(value = "支付方式")
    @EnumDocumentValid(enumClass = OmsOrder.PayType.class, required = false)
    private String payType;

    @ApiModelProperty(value = "订单状态")
    @EnumDocumentValid(enumClass = OmsOrder.OrderStatus.class, required = false)
    private String orderStatus;

    @ApiModelProperty(value = "订单类型")
    @EnumDocumentValid(enumClass = OmsOrder.OrderType.class, required = false)
    private String orderType;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "手机号")
    private String phone;

}


