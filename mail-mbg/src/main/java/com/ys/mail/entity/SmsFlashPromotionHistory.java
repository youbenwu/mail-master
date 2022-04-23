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
 * 秒杀历史记录表
 * </p>
 *
 * @author 070
 * @since 2021-12-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "SmsFlashPromotionHistory对象", description = "秒杀历史记录表")
public class SmsFlashPromotionHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "循环秒杀历史记录表")
    @TableId(value = "histroy_id", type = IdType.INPUT)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long histroyId;

    @ApiModelProperty(value = "置换的产品ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long productId;

    @ApiModelProperty(value = "秒杀到的产品ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long productIdParent;

    @ApiModelProperty(value = "秒杀到产品的价格")
    private Long productIdPrice;

    @ApiModelProperty(value = "每次秒杀到的用户ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "商品图片")
    private String productPic;

    @ApiModelProperty(value = "用户名称")
    private String nickname;

    @ApiModelProperty(value = "抢到商品的用户名称")
    private String newNickname;

    @ApiModelProperty(value = "持有商品的用户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long newUserId;

    @ApiModelProperty(value = "每次秒杀的用户头像")
    private String userPic;

    @ApiModelProperty(value = "持有商品的用户头像")
    private String newUserPic;

    @ApiModelProperty(value = "商品名称")
    private String productName;
}
