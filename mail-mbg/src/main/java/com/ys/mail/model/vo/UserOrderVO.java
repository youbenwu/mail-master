package com.ys.mail.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author ghdhj
 */
@Data
@ApiModel(value = "UserOrderVO对象", description = "用户订单VO")
public class UserOrderVO {

    @ApiModelProperty(value = "订单id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long orderId;

    @ApiModelProperty(value = "应付金额（实际支付金额），100=1元")
    private Long payAmount;

    @ApiModelProperty(value = "商品名称")
    private String productName;

    @ApiModelProperty(value = "支付时间")
    private Date paymentTime;

}
