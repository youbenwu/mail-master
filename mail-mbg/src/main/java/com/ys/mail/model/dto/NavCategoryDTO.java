package com.ys.mail.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

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

    @ApiModelProperty(value = "商品分类名称")
    private String pdtCgyName;
}
