package com.ys.mail.model.dto;

import com.ys.mail.entity.UmsAddress;
import com.ys.mail.model.po.BuyProductPO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**批量购买商品集合对象,本可以和单个一起使用
 * @author ghdhj
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BatchBuyProductDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户收货地址对象")
    private UmsAddress address;

    @ApiModelProperty(value = "商品基本信息")
    private List<BuyProductPO> buyProductPo;
}
