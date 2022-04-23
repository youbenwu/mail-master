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
 * 合伙人产品表
 * </p>
 *
 * @author 070
 * @since 2022-02-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "PmsPartnerProduct对象", description = "合伙人产品表")
public class PmsPartnerProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final Long TOTAL_PRICE_MAX = 4980000L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "partner_pdt_id", type = IdType.INPUT)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long partnerPdtId;

    @ApiModelProperty(value = "商品id")
    private Long productId;

    @ApiModelProperty(value = "合伙人商品名称")
    private String partnerName;

    @ApiModelProperty(value = "价格,使用整数类型Long,1分就是1,100就是1元")
    private Long partnerPrice;

    @ApiModelProperty(value = "保证金,使用整数类型Long,1分就是1,100就是1元")
    private Long earnestMoney;

    @ApiModelProperty(value = "总金额:价格+保证金,1分就是1,100就是1元")
    private Long totalPrice;

    @ApiModelProperty(value = "服务描述")
    private String serveDesc;

    @ApiModelProperty(value = "退还期数(1-12期可以选择)")
    private Integer rePeriods;

    @ApiModelProperty(value = "上下线状态,0->下线,1->上线")
    @TableField("is_publish_status")
    private Boolean publishStatus;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    public PmsPartnerProduct() {
    }

    public PmsPartnerProduct(Long partnerPdtId, Boolean publishStatus) {
        this.partnerPdtId = partnerPdtId;
        this.publishStatus = publishStatus;
    }
}
