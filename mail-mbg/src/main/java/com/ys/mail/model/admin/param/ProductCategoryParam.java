package com.ys.mail.model.admin.param;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author DT
 * @version 1.0
 * @date 2021-11-04 10:29
 */
@Data
public class ProductCategoryParam implements Serializable {

    @ApiModelProperty(value = "主键",required = true)
    @NotNull
    private Long pdtCgyId;

    @ApiModelProperty(value = "上级分类的编号：0表示一级分类",required = true)
    @NotNull
        private Long parentId;

    @ApiModelProperty(value = "产品名称",required = true)
    @NotBlank
    private String pdtCgyName;

    @ApiModelProperty(value = "分类级别：0->1级；1->2级",required = true)
    @NotNull
    private Integer level;

    @ApiModelProperty(value = "是否显示在导航栏：0->不显示；1->显示")
    @TableField("is_nav_status")
    @NotNull
    private Boolean navStatus;

    @ApiModelProperty(value = "显示状态：0->不显示；1->显示")
    @TableField("is_show_status")
    @NotNull
    private Boolean showStatus;

    @ApiModelProperty(value = "是否默认显示首页，只有一个生效：0->不显示；1->显示")
    @NotNull
    @TableField("is_default_status")
    private Boolean defaultStatus;

    @ApiModelProperty(value = "排序")
    @NotNull
    private Integer sort;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "描述")
    private String description;

}
