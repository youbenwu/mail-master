package com.ys.mail.model.po;

import com.ys.mail.entity.SmsFlashPromotion;
import com.ys.mail.model.bo.FlashPromotionProductBO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 组合模式,解决多级映射问题
 * @author DT
 * @version 1.0
 * @date 2021-12-02 10:01
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FlashPromotionProductPO extends SmsFlashPromotion implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "组合的限时购与商品对象")
    private List<FlashPromotionProductBO> bos;

    @ApiModelProperty(value = "合伙人id")
    private Long partnerId;
}
