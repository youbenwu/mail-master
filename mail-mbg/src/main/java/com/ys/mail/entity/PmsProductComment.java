package com.ys.mail.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 商品评价表
 * </p>
 *
 * @author 070
 * @since 2021-11-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="PmsProductComment对象", description="商品评价表")
public class PmsProductComment implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品评论id")
    @TableId(value = "pdt_comment_id", type = IdType.INPUT)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long pdtCommentId;

    @ApiModelProperty(value = "商品id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long productId;

    @ApiModelProperty(value = "评论用户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    @ApiModelProperty(value = "商品评价星数：0->5")
    private Integer productStar;

    @ApiModelProperty(value = "快递包装星数：0->5")
    private Integer courierStar;

    @ApiModelProperty(value = "送货速度星数：0->5")
    private Integer deliveryStar;

    @ApiModelProperty(value = "配送员星数：0->5")
    private Integer markStar;

    @ApiModelProperty(value = "是否匿名,0->false,1->true")
    @TableField("is_anonymous")
    private Boolean anonymous;

    @ApiModelProperty(value = "商品评论内容")
    private String productContent;

    @ApiModelProperty(value = "购买时的商品属性")
    private String productAttribute;

    @ApiModelProperty(value = "上传图片地址;以逗号隔开,最多5张")
    private String pics;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date updateTime;


}
