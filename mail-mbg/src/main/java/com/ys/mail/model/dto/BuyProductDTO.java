package com.ys.mail.model.dto;

import com.ys.mail.entity.UmsAddress;
import com.ys.mail.model.po.BuyProductPO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author DT
 * @version 1.0
 * @date 2021-11-29 09:49
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuyProductDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户收货地址对象")
    private UmsAddress address;

    @ApiModelProperty(value = "商品基本信息")
    private BuyProductPO buyProductPo;

}
