package com.ys.mail.model.dto;

import com.ys.mail.entity.SmsFlashPromotion;
import com.ys.mail.entity.SmsFlashPromotionProduct;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author DT
 * @version 1.0
 * @date 2021-11-25 18:08
 */
@Data
public class SecondProductDTO extends SmsFlashPromotion implements Serializable {

    @ApiModelProperty(value = "限时秒杀中的商品")
    private List<FlashPromotionProductDTO> promotionProductDTOList;

    @ApiModelProperty(value = "公司秒杀中的商品")
    private List<FlashPromotionProductDTO> cpyProducts;
}
