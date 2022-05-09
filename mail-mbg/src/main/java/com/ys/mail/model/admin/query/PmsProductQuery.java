package com.ys.mail.model.admin.query;

import com.ys.mail.annotation.BlankOrPattern;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Desc 订单查询对象
 * @Author CRH
 * @Create 2021-12-27 13:48
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "PmsProductQuery", description = "后台商品查询接口参数")
public class PmsProductQuery extends Query {

    @ApiModelProperty(value = "商品名称")
    private String productName;

    @ApiModelProperty(value = "商品分类id")
    @BlankOrPattern(regexp = "^\\d{19}$", message = "ID不合法，请检查！")
    private String pdtCgyId;

    @ApiModelProperty(value = "商品属性分类id")
    @BlankOrPattern(regexp = "^\\d{19}$", message = "ID不合法，请检查！")
    private String pdtAttributeCgyId;

    @ApiModelProperty(value = "上架状态：0->下架；1->上架")
    @BlankOrPattern(regexp = "^[01]$", message = "上架状态：0->下架；1->上架")
    private String publishStatus;

    @ApiModelProperty(value = "逻辑删除：0->未删除,1->删除")
    @BlankOrPattern(regexp = "^[01]$", message = "逻辑删除：0->未删除,1->删除")
    private String deleted;

    @ApiModelProperty(value = "促销类型：0->没有促销使用原价;1->使用促销价；2->使用会员价；3->使用阶梯价格；4->使用满减价格；5->限时购;6->拼团购;7->礼品;8->合伙人商品")
    private Integer promotionType;

    @ApiModelProperty(value = "是否合伙人产品：0->false，1->true，默认所有")
    private Boolean isPartner;

}


