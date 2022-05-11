package com.ys.mail.model.admin.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Future;
import java.io.Serializable;
import java.util.Date;

/**
 * 组合设计模式
 * @author DT
 * @version 1.0
 * @date 2022-01-22 18:30
 */
@Data
public class PcFlashPdtDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long productId;

    @ApiModelProperty(value = "商品名称")
    private String productName;

    @ApiModelProperty(value = "商品图片")
    private String pic;

    @ApiModelProperty(value = "场次主键id")
    private Long flashPromotionId;

    @ApiModelProperty(value = "场次名称")
    private String flashPromotionTitle;

    @ApiModelProperty(value = "场次开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "场次结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "是否展示于首页:0->false,1->true")
    @TableField("is_home_status")
    private Boolean homeStatus;

    @ApiModelProperty(value = "0->大尾狐,1->呼啦兔场次")
    private Byte cpyType;

    @ApiModelProperty(value = "秒杀商品主键id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long flashPromotionPdtId;

    @ApiModelProperty(value = "秒杀商品价格")
    private Long flashPromotionPrice;

    @ApiModelProperty(value = "限购数量")
    private Long flashPromotionCount;

    @ApiModelProperty(value = "用户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    @ApiModelProperty(value = "上架状态：0->下架；1->上架")
    @TableField("is_publish_status")
    private Boolean publishStatus;

    @ApiModelProperty(value = "秒杀商品状态：1->已卖出；2->秒杀中；3->上架")
    private Integer flashProductStatus;

    @ApiModelProperty(value = "记录原价格")
    private Long flashPromotionOriginPrice;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "每人限购数量")
    private Integer flashPromotionLimit;

    @ApiModelProperty(value = "合伙人价格")
    private Long partnerPrice;

    @ApiModelProperty(value = "截止时间")
    private Date expireTime;
}
