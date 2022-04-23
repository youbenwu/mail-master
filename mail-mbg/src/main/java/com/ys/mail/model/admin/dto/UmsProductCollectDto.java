package com.ys.mail.model.admin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UmsProductCollectDto implements Serializable {

    @ApiModelProperty(value = "用户收藏中间表主键")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long pdtCollectId;

    @ApiModelProperty(value = "用户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    @ApiModelProperty(value = "产品id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long productId;

    @ApiModelProperty(value = "收藏时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "头像")
    private String headPortrait;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "微信OPENID")
    private String openid;

    @ApiModelProperty(value = "品牌id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long brandId;

    @ApiModelProperty(value = "商品分类id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long pdtCgyId;

    @ApiModelProperty(value = "商品名称")
    private String productName;

    @ApiModelProperty(value = "主图")
    private String pic;

    @ApiModelProperty(value = "角色:0普通用户用户,1高级用户")
    private Integer roleId;

    @ApiModelProperty(value = "货号")
    private String productSn;

    @ApiModelProperty(value = "销量")
    private Integer sale;

    @ApiModelProperty(value = "价格,使用整数类型Long,1分就是1,100就是1元")
    private Long price;

    @ApiModelProperty(value = "商品描述")
    private String description;

    @ApiModelProperty(value = "市场价,使用整数类型Long,1分就是1,100就是1元")
    private Long originalPrice;

    @ApiModelProperty(value = "库存")
    private Integer stock;

    @ApiModelProperty(value = "单位")
    private String unit;
}
