package com.ys.mail.model.admin.vo;

import com.ys.mail.entity.OmsOrder;
import com.ys.mail.model.vo.OmsOrderItemVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 订单详情
 *
 * @author CRH
 * @date 2022-05-30 15:45
 * @since 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OrderDetailsVO extends OmsOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "订单所包含的商品")
    private List<OmsOrderItemVO> omsOrderItem;

}
