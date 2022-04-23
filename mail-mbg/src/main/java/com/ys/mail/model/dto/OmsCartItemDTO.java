package com.ys.mail.model.dto;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ys.mail.entity.OmsCartItem;
import com.ys.mail.entity.PmsProduct;
import com.ys.mail.entity.SmsFlashPromotion;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author DT
 * @version 1.0
 * @date 2021-11-13 17:37
 */
@Data
public class OmsCartItemDTO extends OmsCartItem implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "商品名称")
    private String productName;

    @ApiModelProperty(value = "商品销售属性，json格式")
    private String spData;

    @ApiModelProperty(value = "商品图片")
    private String pic;

    @ApiModelProperty(value = "商品状态")
    private Boolean publishStatus;
}
