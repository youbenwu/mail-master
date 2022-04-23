package com.ys.mail.model.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ys.mail.entity.PmsProductAttribute;
import com.ys.mail.entity.PmsProductComment;
import com.ys.mail.entity.SmsFlashPromotion;
import com.ys.mail.entity.UmsAddress;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 秒杀商品详情页面-单一规格
 * @author DT
 * @version 1.0
 * @date 2021-12-20 10:31
 */
@Data
public class QuickBuyProductInfoDTO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品属性参数集合")
    private List<PmsProductAttribute> pmsProductAttributes;

    @ApiModelProperty(value = "评论集合")
    private List<PmsProductComment> productComments;

    @ApiModelProperty(value = "收货地址")
    private UmsAddress address;

    @ApiModelProperty(value = "收藏id,0为不收藏,19位雪花id就是收藏")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long pdtCollectId;

    @ApiModelProperty(value = "秒杀商品主键id")
    @TableId(value = "flash_promotion_pdt_id", type = IdType.INPUT)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long flashPromotionPdtId;

    @ApiModelProperty(value = "限时购id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long flashPromotionId;

    @ApiModelProperty(value = "限时购场次id,需求增加的时候再用,暂时不用")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long flashPromotionSnId;

    @ApiModelProperty(value = "商品id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long productId;

    @ApiModelProperty(value = "品牌id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long brandId;

    @ApiModelProperty(value = "商品分类id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long pdtCgyId;

    @ApiModelProperty(value = "商品名称")
    private String productName;

    @ApiModelProperty(value = "商品主图")
    private String pic;

    @ApiModelProperty(value = "销量")
    private Integer sale;

    @ApiModelProperty(value = "商品描述")
    private String description;

    @ApiModelProperty(value = "副标题,可以为空")
    private String subTitle;

    @ApiModelProperty(value = "画册图片")
    private String albumPics;

    @ApiModelProperty(value = "详情页,富文本")
    private String detailDesc;

    @ApiModelProperty(value = "限时购价格,1分等于1,100等于1元,限时购展示价格")
    private Long flashPromotionPrice;

    @ApiModelProperty(value = "限时购数量")
    private Integer flashPromotionCount;

    @ApiModelProperty(value = "每人限购数量")
    private Integer flashPromotionLimit;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "当前持有用户ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    @ApiModelProperty(value = "发布者ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long publisherId;

    @ApiModelProperty(value = "skuID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long skuStockId;

    @ApiModelProperty(value = "当前秒杀轮数，例如：第3轮")
    private Integer flashPromotionRound;

    @ApiModelProperty(value = "上架状态：0->下架；1->上架")
    @TableField("is_publish_status")
    private Boolean publishStatus;

    @ApiModelProperty(value = "秒杀商品状态：1->已卖出；2->秒杀中；3->上架")
    private Integer flashProductStatus;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "订单id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long orderId;

    @ApiModelProperty(value = "秒杀场次对象")
    private SmsFlashPromotion flashPromotion;

    @ApiModelProperty(value = "合伙人id")
    private Long partnerId;

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "区")
    private String region;

    @ApiModelProperty(value = "详细地址")
    private String shopAddress;


}
