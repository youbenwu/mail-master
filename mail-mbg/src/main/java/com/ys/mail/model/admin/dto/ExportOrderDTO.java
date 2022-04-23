package com.ys.mail.model.admin.dto;

import com.ys.mail.entity.OmsOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Desc
 * @Author CRH
 * @Create 2022-01-05 21:35
 */
@Data
public class ExportOrderDTO extends OmsOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "商品名称")
    private String productName;

    @ApiModelProperty(value = "存储的整数类型,1元就是100")
    private Long productPrice;

    @ApiModelProperty(value = "购买数量")
    private Integer productQuantity;

}
