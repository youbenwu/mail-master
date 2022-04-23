package com.ys.mail.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author DT
 * @version 1.0
 * @date 2021-12-13 09:28
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class GenerateOrderBO implements Serializable {

    @ApiModelProperty(value = "订单编号")
    private String orderSn;

    @ApiModelProperty(value = "金额,1:100,1元实际等于0.01")
    private String amount;
}
