package com.ys.mail.model.param;

import com.ys.mail.entity.OmsOrder;
import com.ys.mail.entity.OmsOrderItem;
import com.ys.mail.entity.UmsUser;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.IdGenerator;
import com.ys.mail.util.IdWorker;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author ghdhj
 */
@Data
public class PartnerGenerateOrderParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "图片", required = true)
    @NotBlank(message = "图片不能为空")
    private String pic;

    @ApiModelProperty(value = "数量", required = true)
    @NotNull
    private Integer quantity = 1;

    @ApiModelProperty(value = "留言")
    private String orderNote;

    @ApiModelProperty(value = "总金额", required = true)
    @NotNull
    @Min(value = 2)
    private Long totalPrice;

    @ApiModelProperty(value = "服务费", required = true)
    @NotNull
    @Min(value = 1)
    private Long partnerPrice;

    @ApiModelProperty(value = "保证金", required = true)
    @NotNull
    @Min(value = 1)
    private Long earnestMoney;

    @ApiModelProperty(value = "商品id", required = true)
    @NotBlank
    @Pattern(regexp = "^\\d{19}$")
    private String productId;

    @ApiModelProperty(value = "商品名称", required = true)
    @NotBlank
    private String productName;

    @ApiModelProperty(value = "合伙人商品主键id", required = true)
    @NotBlank
    @Pattern(regexp = "^\\d{19}$")
    private String partnerPdtId;

    @ApiModelProperty(value = "0->大尾狐,1->呼啦兔")
    @NotNull
    @Range(min = 0, max = 1)
    private Byte cpyType;


    public PartnerGenerateOrderParam() {
    }


    public OmsOrder getParam(UmsUser user) {
        return OmsOrder.builder()
                .orderId(IdWorker.generateId())
                .orderSn(IdGenerator.INSTANCE.generateId())
                .userId(user.getUserId())
                .receiverPhone(user.getPhone())
                .receiverName(BlankUtil.isEmpty(user.getAlipayName()) ? user.getNickname() : user.getAlipayName())
                .totalAmount(totalPrice)
                .payAmount(totalPrice)
                .orderType(OmsOrder.OrderType.FOUR.key())
                .orderNote(orderNote)
                .cpyType(cpyType)
                .build();
    }

    public OmsOrderItem getParam(OmsOrder order) {
        return OmsOrderItem.builder()
                .orderItemId(IdWorker.generateId())
                .orderId(order.getOrderId())
                .orderSn(order.getOrderSn())
                .productId(Long.valueOf(productId))
                .productPic(pic)
                .productName(productName)
                .productPrice(totalPrice)
                .productQuantity(quantity)
                .partnerPdtId(Long.valueOf(partnerPdtId))
                .build();
    }
}
