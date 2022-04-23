package com.ys.mail.model.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ys.mail.entity.SmsFlashPromotion;
import com.ys.mail.entity.SmsFlashPromotionProduct;
import com.ys.mail.model.dto.FlashPromotionProductDTO;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author DT
 * @version 1.0
 * @date 2021-11-12 17:18
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FlashPromotionProductVO extends SmsFlashPromotion implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "限时购商品集合对象")
    private List<FlashPromotionProductDTO> dtoList;

}
