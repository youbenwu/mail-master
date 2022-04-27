package com.ys.mail.model.bo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author DT
 * @version 1.0
 * @date 2021-12-02 10:19
 */
@Data
public class FlashPromotionProductBO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "flash_promotion_pdt_id", type = IdType.INPUT)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long flashPromotionPdtId;

    @ApiModelProperty(value = "限时购id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long flashPromotionId;

    @ApiModelProperty(value = "商品id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long productId;

    @ApiModelProperty(value = "商品名称")
    private String productName;

    @ApiModelProperty(value = "主图")
    private String pic;

    @ApiModelProperty(value = "商品原价")
    private Long price;

    @ApiModelProperty(value = "限时购场次id,需求增加的时候再用,暂时不用")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long flashPromotionSnId;

    @ApiModelProperty(value = "限时购价格,1分等于1,100等于1元")
    private Long flashPromotionPrice;

    @ApiModelProperty(value = "限时购数量")
    private Integer flashPromotionCount;

    @ApiModelProperty(value = "每人限购数量")
    private Integer flashPromotionLimit;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "合伙人id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long partnerId;

    @ApiModelProperty(value = "距离，单位m")
    private Double distance;

}
