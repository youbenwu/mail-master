package com.ys.mail.model.dto;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ys.mail.entity.PmsProduct;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author DT
 * @version 1.0
 * @date 2021-11-13 17:37
 */
@Data
public class GroupBuyDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "group_buy_id", type = IdType.INPUT)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long groupBuyId;

    @ApiModelProperty(value = "商品对象")
    private PmsProduct product;

    @ApiModelProperty(value = "展示价,使用Long类型,1元就是表示100")
    private Long showPrice;

    @ApiModelProperty(value = "限购数量")
    private Integer limitNum;

    @ApiModelProperty(value = "已开团数量")
    private Integer groupNum;

    @ApiModelProperty(value = "开团模式 1普通2阶梯")
    private Integer pattern;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "上下线状态:0->false,1->true")
    @TableField("is_publish_status")
    private Boolean publishStatus;

    @ApiModelProperty(value = "购买次数限制")
    private Integer triesNum;

    @ApiModelProperty(value = "活动截止时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date deadlineTime;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "逻辑删除：0->未删除,1->删除")
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleted;
}
