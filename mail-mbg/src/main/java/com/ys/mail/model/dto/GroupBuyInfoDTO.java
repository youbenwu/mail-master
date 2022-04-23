package com.ys.mail.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ys.mail.entity.GroupSponsor;
import com.ys.mail.entity.PmsProductAttribute;
import com.ys.mail.entity.PmsProductComment;
import com.ys.mail.entity.UmsAddress;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.elasticsearch.common.recycler.Recycler;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author DT
 * @version 1.0
 * @date 2021-12-28 16:21
 */
@Data
public class GroupBuyInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品属性参数集合")
    private List<PmsProductAttribute> pmsProductAttributes;

    @ApiModelProperty(value = "评论集合")
    private List<PmsProductComment> productComments;

    @ApiModelProperty(value = "收货地址对象")
    private UmsAddress address;

    @ApiModelProperty(value = "收藏id,0为不收藏,19位雪花id就是收藏")
    private Long pdtCollectId;

    @ApiModelProperty(value = "拼团购主键id")
    private Long groupBuyId;

    @ApiModelProperty(value = "商品id")
    private Long  productId;

    @ApiModelProperty(value = "展示价,使用Long类型,1元就是表示100")
    private Long showPrice;

    @ApiModelProperty(value = "限购数量")
    private Integer limitNum;

    @ApiModelProperty(value = "已开团数量")
    private Integer groupNum;

    @ApiModelProperty(value = "上下线状态")
    @TableField("is_publish_status")
    private Boolean publishStatus;

    @ApiModelProperty(value = "购买次数限制")
    private Integer triesNum;

    @ApiModelProperty(value = "活动截止时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date deadlineTime;

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

    @ApiModelProperty(value = "商品原价")
    private Long price;

    /**
     * 为前端做了单向,本来应该是可以选择的拼团列表,每次拼团成功就往人数上加一次,直到拼满,关联的一个发起id,只有一个true
     * 简单的给前端做吧,不显示拼团列表只能这样了
     */
    @ApiModelProperty(value = "拼团发起,前端根据这个做判断,为空就显示发起拼团,不为空就显示拼团中的人头数去拼")
    private List<GroupSponsor> sponsors;
}
