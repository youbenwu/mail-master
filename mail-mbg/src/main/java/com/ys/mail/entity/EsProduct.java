package com.ys.mail.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Description: 商品信息表
 *
 * @author lcc
 * Date:Fri Nov 19 16:03:38 CST 2021
 */
@Data
@Document(indexName = "pms", type = "_doc", shards = 1, replicas = 0)
public class EsProduct implements Serializable {

	@ApiModelProperty(value = "主键id")
	@Id
	private long productId;

	@ApiModelProperty(value = "商品名称")
	@Field(type = FieldType.Keyword)
	private String productName;

	@ApiModelProperty(value = "货号")
	@Field(type = FieldType.Keyword)
	private String productSn;

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

	@ApiModelProperty(value = "主图")
	private String pic;

	@ApiModelProperty(value = "上架状态：0->下架；1->上架")
	private Boolean publishStatus;

	@ApiModelProperty(value = "新品状态:0->不是新品；1->新品")
	private Boolean newStatus;

	@ApiModelProperty(value = "推荐状态；0->不推荐；1->推荐")
	private Boolean recommendStatus;

	@ApiModelProperty(value = "审核状态：0->未审核；1->审核通过")
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

	@ApiModelProperty(value = "详情页,富文本")
	private String detailDesc;

	@ApiModelProperty(value = "促销开始时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
	private Date promotionStartTime;

	@ApiModelProperty(value = "促销结束时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
	private Date promotionEndTime;

	@ApiModelProperty(value = "活动限购数量")
	private Integer promotionPerLimit;

	@ApiModelProperty(value = "促销类型：0->没有促销使用原价;1->使用促销价；2->使用会员价；3->使用阶梯价格；4->使用满减价格；5->限时购;6->拼团购")
	private Integer promotionType;

	@ApiModelProperty(value = "创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
	private Date createTime;

	@ApiModelProperty(value = "修改时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
	private Date updateTime;

	@ApiModelProperty(value = "逻辑删除：0->未删除,1->删除")
	private Integer deleted;

	@ApiModelProperty(value = "是否精选,0->不精选,1->精选")
	private Boolean handpickStatus;

	@ApiModelProperty(value = "精致:0->false,1->true")
	private Boolean delicacyStatus;

	@ApiModelProperty(value = "穿搭:0->false,1->true")

	private Boolean styleStatus;

	@ApiModelProperty(value = "是否生活：0->false,1->true")
	private Boolean liveStatus;

	@ApiModelProperty(value = "评论数,默认为0")
	private Integer commentSum;
}
