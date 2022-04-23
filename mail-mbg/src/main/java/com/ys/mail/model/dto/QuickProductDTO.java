package com.ys.mail.model.dto;

import com.ys.mail.entity.UmsAddress;
import com.ys.mail.model.po.QuickProductPO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author DT
 * @version 1.0
 * @date 2021-12-22 09:45
 */
@Data
public class QuickProductDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户收货地址对象")
    private UmsAddress address;

    @ApiModelProperty(value = "秒杀商品的基本信息")
    private QuickProductPO po;

    public QuickProductDTO(UmsAddress address, QuickProductPO po) {
        this.address = address;
        this.po = po;
    }

    private QuickProductDTO() {}
}
