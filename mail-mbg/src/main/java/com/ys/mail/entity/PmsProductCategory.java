package com.ys.mail.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 产品分类表
 * </p>
 *
 * @author 070
 * @since 2021-11-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="PmsProductCategory对象", description="产品分类表")
public class PmsProductCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "pdt_cgy_id", type = IdType.INPUT)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long pdtCgyId;

    @ApiModelProperty(value = "上级分类的编号：0表示一级分类")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long parentId;

    @ApiModelProperty(value = "商品分类名称")
    private String pdtCgyName;

    @ApiModelProperty(value = "分类级别：0->1级；1->2级")
    private Integer level;

    @ApiModelProperty(value = "点击数")
    private Integer productCount;

    @ApiModelProperty(value = "是否显示在导航栏：0->不显示；1->显示")
    @TableField("is_nav_status")
    private Boolean navStatus;

    @ApiModelProperty(value = "显示状态：0->不显示；1->显示")
    @TableField("is_show_status")
    private Boolean showStatus;

    @ApiModelProperty(value = "是否默认显示首页：0->不显示；1->显示")
    @TableField("is_default_status")
    private Boolean defaultStatus;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "逻辑删除,0->未删除,1->删除")
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleted;
}
