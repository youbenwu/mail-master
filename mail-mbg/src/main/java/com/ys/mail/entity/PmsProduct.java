package com.ys.mail.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 商品信息表
 * </p>
 *
 * @author 070
 * @since 2021-11-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "PmsProduct对象", description = "商品信息表")
public class PmsProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "product_id", type = IdType.INPUT)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long productId;

    @ApiModelProperty(value = "品牌id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long brandId;

    @ApiModelProperty(value = "商品分类id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long pdtCgyId;

    @ApiModelProperty(value = "运费模板id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long freightTemplateId;

    @ApiModelProperty(value = "产品属性分类id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long pdtAttributeCgyId;

    @ApiModelProperty(value = "商品名称")
    private String productName;

    @ApiModelProperty(value = "主图")
    private String pic;

    @ApiModelProperty(value = "货号")
    private String productSn;

    @ApiModelProperty(value = "上架状态：0->下架；1->上架")
    @TableField("is_publish_status")
    private Boolean publishStatus;

    @ApiModelProperty(value = "新品状态:0->不是新品；1->新品")
    @TableField("is_new_status")
    private Boolean newStatus;

    @ApiModelProperty(value = "推荐状态；0->不推荐；1->推荐")
    @TableField("is_recommend_status")
    private Boolean recommendStatus;

    @ApiModelProperty(value = "审核状态：0->未审核；1->审核通过")
    @TableField("is_verify_status")
    private Boolean verifyStatus;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "销量")
    private Integer sale;

    @ApiModelProperty(value = "价格,使用整数类型Long,1分就是1,100就是1元")
    private Long price;

    @ApiModelProperty(value = "促销价格,使用整数类型Long,1分就是1,100就是1元")
    private Long promotionPrice;

    @ApiModelProperty(value = "赠送的成长值")
    private Integer giftGrowth;

    @ApiModelProperty(value = "赠送的积分")
    private Integer giftPoint;

    @ApiModelProperty(value = "限制使用的积分数")
    private Integer usePointLimit;

    @ApiModelProperty(value = "副标题,可以为空")
    private String subTitle;

    @ApiModelProperty(value = "商品描述")
    private String description;

    @ApiModelProperty(value = "市场价,使用整数类型Long,1分就是1,100就是1元")
    private Long originalPrice;

    @ApiModelProperty(value = "库存")
    private Integer stock;

    @ApiModelProperty(value = "库存预警值")
    private Integer lowStock;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "商品重量，默认为克")
    private BigDecimal weight;

    @ApiModelProperty(value = "是否为预告商品：0->不是；1->是")
    private Integer previewStatus;

    @ApiModelProperty(value = "以逗号分割的产品服务：1->无忧退货；2->快速退款；3->免费包邮")
    private String serviceIds;

    @ApiModelProperty(value = "画册图片，连产品图片限制为5张，以逗号分割")
    private String albumPics;

    @ApiModelProperty(value = "详情描述")
    private String detailTitle;

    @ApiModelProperty(value = "用户购买须知")
    private String purchaseNote;

    @ApiModelProperty(value = "详情页,富文本")
    private String detailDesc;

    @ApiModelProperty(value = "促销开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date promotionStartTime;

    @ApiModelProperty(value = "促销结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date promotionEndTime;

    @ApiModelProperty(value = "活动限购数量")
    private Integer promotionPerLimit;

    @ApiModelProperty(value = "促销类型：0->没有促销使用原价;1->使用促销价；2->使用会员价；3->使用阶梯价格；4->使用满减价格；5->限时购;6->拼团购;7->礼品;8->创客商品")
    private Integer promotionType;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "逻辑删除：0->未删除,1->删除")
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleted;

    @ApiModelProperty(value = "评论数,默认为0")
    private Integer commentSum;

    @ApiModelProperty(value = "合伙人id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long partnerId;

    @ApiModelProperty(value = "会员展示价格,一元就是100")
    private Long mebPrice;

    @ApiModelProperty(value = "折扣比例:0.55->就是55%")
    private BigDecimal disCount;

    @ApiModelProperty(value = "会员特惠：0->false,1->true")
    @TableField("is_live_status")
    private Boolean liveStatus;

    @ApiModelProperty(value = "会员尾品：0->false,1->true")
    @TableField("is_handpick_status")
    private Boolean handpickStatus;

    @ApiModelProperty(value = "潮品专区：0->false,1->true")
    @TableField("is_delicacy_status")
    private Boolean delicacyStatus;

    @ApiModelProperty(value = "甄选好物：0->false,1->true")
    @TableField("is_style_status")
    private Boolean styleStatus;

    @ApiModelProperty(value = "首页推荐：0->false,1->true")
    @TableField("is_home_status")
    private Boolean homeStatus;


}
