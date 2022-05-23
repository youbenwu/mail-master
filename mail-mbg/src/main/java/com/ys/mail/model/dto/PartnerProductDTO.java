package com.ys.mail.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ghdhj
 */
@Data
public class PartnerProductDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long partnerPdtId;

    @ApiModelProperty(value = "商品id")
    private Long productId;

    @ApiModelProperty(value = "合伙人商品名称")
    private String partnerName;

    @ApiModelProperty(value = "总金额:价格+保证金,1分就是1,100就是1元")
    private Long totalPrice;

    @ApiModelProperty(value = "上下线状态,0->下线,1->上线")
    @TableField("is_publish_status")
    private Boolean publishStatus;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "销量")
    private Integer sale;

    @ApiModelProperty(value = "主图")
    private String pic;

    @ApiModelProperty(value = "价格")
    private Long partnerPrice;

    @ApiModelProperty(value = "保证金")
    private Long earnestMoney;

    @ApiModelProperty(value = "副标题")
    private String subTitle;
}
