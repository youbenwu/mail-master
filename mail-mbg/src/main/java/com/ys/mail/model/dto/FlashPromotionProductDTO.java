package com.ys.mail.model.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ys.mail.entity.PmsProduct;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author DT
 * @version 1.0
 * @date 2021-11-12 16:24
 */
@Data
public class FlashPromotionProductDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "flash_promotion_pdt_id", type = IdType.INPUT)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long flashPromotionPdtId;

    @ApiModelProperty(value = "限时购id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long flashPromotionId;

    @ApiModelProperty(value = "商品对象")
    private PmsProduct product;

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

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "商品标题")
    private String subTitle;

    @ApiModelProperty(value = "商品原价")
    private Long price;

    @ApiModelProperty(value = "规格信息")
    private String spData;

    @ApiModelProperty(value = "当前秒杀轮数，例如：第3轮")
    private Integer flashPromotionRound;

    @ApiModelProperty(value = "商家分佣比例,百分比")
    private BigDecimal merchantOrder;

    @ApiModelProperty(value = "系统分佣比例,百分比")
    private BigDecimal systemOrder;

    @ApiModelProperty(value = "秒杀开始时间")
    private Date startTime;

    @ApiModelProperty(value = "秒杀商品状态：1->已卖出；2->秒杀中")
    private Integer flashProductStatus;

    @ApiModelProperty(value = "上架状态：0->下架；1->上架")
    @TableField("is_publish_status")
    private Boolean publishStatus;

    @ApiModelProperty(value = "图片地址")
    private String pic;

    @ApiModelProperty(value = "发布者")
    private String publisherName;

    @ApiModelProperty(value = "持有者")
    private String nickname;

    @ApiModelProperty(value = "发布者头像")
    private String publisherPic;

    @ApiModelProperty(value = "持有者头像")
    private String nickPic;

    @ApiModelProperty(value = "订单id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long orderId;

    @ApiModelProperty(value = "核销状态")
    private Integer isStatus;

    @ApiModelProperty(value = "合伙人id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long partnerId;

    @ApiModelProperty(value = "合伙人手机号")
    private String partnerPhone;

    @ApiModelProperty(value = "合伙人地址")
    private String partnerAddress;

    @ApiModelProperty(value = "用户购买须知")
    private String purchaseNote;

}
