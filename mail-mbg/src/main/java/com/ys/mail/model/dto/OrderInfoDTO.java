package com.ys.mail.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ys.mail.model.vo.OrderItemDetailsVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author DT
 * @version 1.0
 * @date 2021-12-23 19:51
 */
@Data
public class OrderInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long orderId;

    @ApiModelProperty(value = "用户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    @ApiModelProperty(value = "订单编号")
    private String orderSn;

    @ApiModelProperty(value = "支付方式：0->未支付；1云闪付,2支付宝")
    private Integer payType;

    @ApiModelProperty(value = "订单类型：0->正常订单；1->秒杀订单;2->拼团订单")
    private Integer orderType;

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

    @ApiModelProperty(value = "支付时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date paymentTime;

    @ApiModelProperty(value = "订单状态：0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单")
    private Integer orderStatus;

    @ApiModelProperty(value = "订单总金额,1元就是100")
    private Long totalAmount;

    @ApiModelProperty(value = "物流公司")
    private String deliveryCompany;

    @ApiModelProperty(value = "物流编号")
    private String deliverySn;

    @ApiModelProperty(value = "物流公司名称")
    private String kdName;

    @ApiModelProperty(value = "订单商品信息")
    private List<OrderItemDetailsVO> omsOrderItem;

    @ApiModelProperty(value = "公司类型订单:0->大尾狐,1->呼啦兔")
    private Byte cpyType;

    @ApiModelProperty(value = "核销编码")
    private String code;

    @ApiModelProperty(value = "核销码状态   0待使用 1已使用 2已过期")
    private String isStatus;

    @ApiModelProperty(value = "合伙人价格")
    private Long partnerPrice;

    @ApiModelProperty(value = "省")
    private String province;
    @ApiModelProperty(value = "市")
    private String city;
    @ApiModelProperty(value = "区")
    private String region;
    @ApiModelProperty(value = "详细地址")
    private String address;
    @ApiModelProperty(value = "纬度")
    private Double latitude;
    @ApiModelProperty(value = "经度")
    private Double longitude;

    @ApiModelProperty(value = "合伙人店铺名称")
    private String corporateName;

    @ApiModelProperty(value = "合伙人手机号")
    private String phone;

    @ApiModelProperty(value = "合伙人头像")
    private String headPortrait;

    @ApiModelProperty(value = "截止时间")
    private Date expireTime;

}
