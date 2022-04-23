package com.ys.mail.model.admin.dto.excel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Desc 用户个人秒杀商品详情数据
 * @Author CRH
 * @Create 2022-03-30 16:44
 */
@Data
@ApiModel(value = "UserSecKillDTO")
public class UserSecKillDetailsDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "秒杀商品ID")
    private Long flashPromotionPdtId;

    @ApiModelProperty(value = "秒杀场次名称")
    private String flashPromotionTitle;

    @ApiModelProperty(value = "商品名称")
    private String productName;

    @ApiModelProperty(value = "发布价格")
    private Double releasePrice;

    @ApiModelProperty(value = "商品原价")
    private Double originalPrice;

    @ApiModelProperty(value = "实收价格")
    private Double totalAmount;

    @ApiModelProperty(value = "商品数量")
    private Integer flashPromotionCount;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "秒杀商品状态")
    private Integer flashProductStatus;

    @ApiModelProperty(value = "客户端类型")
    private Integer cpyType;

}
