package com.ys.mail.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ys.mail.model.admin.tree.PcProductCategoryTree;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.List;

/**
 * 导航分类对象
 *
 * @author CRH
 * @date 2022-04-30 18:32
 * @since 1.0
 */
@Data
@ApiModel(value = "NavCategoryDTO", description = "导航分类对象")
public class NavCategoryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "分类id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long pdtCgyId;

    @ApiModelProperty(value = "上级分类id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Long parentId;

    @ApiModelProperty(value = "商品分类名称")
    private String pdtCgyName;

    @ApiModelProperty(value = "商品分类图标")
    private String icon;

    @ApiModelProperty(value = "子节点")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<PcProductCategoryTree> children;

    @ApiModelProperty(value = "商品分类排序")
    private Integer sort;
}
