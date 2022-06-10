package com.ys.mail.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ys.mail.enums.IPairs;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 商品限时购与商品关系表
 * </p>
 *
 * @author 070
 * @since 2021-11-12
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "SmsFlashPromotionProduct对象", description = "商品限时购与商品关系表")
public class SmsFlashPromotionProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
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

    @ApiModelProperty(value = "限时购价格,1分等于1,100等于1元,限时购展示价格")
    private Long flashPromotionPrice;

    @ApiModelProperty(value = "限时购数量")
    private Integer flashPromotionCount;

    @ApiModelProperty(value = "原总数量")
    private Integer flashPromotionOriginCount;

    @ApiModelProperty(value = "当前发布产品原价格")
    private Long flashPromotionOriginPrice;

    @ApiModelProperty(value = "每人限购数量")
    private Integer flashPromotionLimit;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "当前持有用户ID")
    private Long userId;

    @ApiModelProperty(value = "发布者ID")
    private Long publisherId;

    @ApiModelProperty(value = "skuID")
    private Long skuStockId;

    @ApiModelProperty(value = "当前秒杀轮数，例如：第3轮")
    private Integer flashPromotionRound;

    @ApiModelProperty(value = "上架状态：0->下架；1->上架")
    private Boolean isPublishStatus;

    @ApiModelProperty(value = "秒杀商品状态：1->已卖出；2->秒杀中；3->上架；4->待卖出;5->已退款")
    private Integer flashProductStatus;

    @ApiModelProperty(value = "公司类型订单:0->大尾狐,1->呼啦兔")
    private Byte cpyType;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "订单id")
    private Long orderId;

    @ApiModelProperty(value = "用户实际收款金额")
    private Long totalAmount;

    @ApiModelProperty(value = "合伙人价格")
    private Long partnerPrice;

    @ApiModelProperty(value = "商品店铺对象")
    private String pdtStoreObj;

    @ApiModelProperty(value = "截止时间，作用于合伙人商品，过期则不能上架、核销等")
    private Date expireTime;

    @Getter
    @AllArgsConstructor
    @Accessors(fluent = true)
    public enum FlashProductStatus implements IPairs<Integer, String, FlashProductStatus> {
        /**
         * 状态
         */
        MINUS_ONE(-1, "已过期"),
        ONE(1, "已卖出"),
        TWO(2, "秒杀中"),
        THREE(3, "上架"),
        FOUR(4, "待卖出"),
        FIVE(5, "已退款"),
        ;
        final Integer key;
        final String value;
    }

    @ApiModelProperty(value = "未卖出的次数,默认值为0")
    private Integer num;

}