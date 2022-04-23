package com.ys.mail.model.admin.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author DT
 * @version 1.0
 * @date 2021-10-20 17:24
 */
@Data
public class PcMenuParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "菜单id",required = true)
    @NotNull
    private Long menuId;

    @ApiModelProperty(value = "父级菜单id,0为一级",required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @NotNull
    private Long parentId;

    @ApiModelProperty(value = "菜单名称",required = true)
    @NotBlank
    private String menuName;

    @ApiModelProperty(value = "菜单url",required = true)
    @NotBlank
    @Pattern(regexp = "^/.*|^#",message = "url路径必须以/开始")
    private String menuUrl;

    @ApiModelProperty(value = "url请求的方法",required = true)
    @NotBlank
    private String httpMethod;

    @ApiModelProperty(value = "权限标识符",required = true)
    @Pattern(regexp = "^ROLE_.*|^#",message = "权限标识符必须以ROLE_开始")
    @NotBlank
    private String perms;

    @ApiModelProperty(value = "排序,可以定义子菜单的排序",required = true)
    @NotNull
    @Min(value = 0)
    private Integer orderNum;

    @ApiModelProperty(value = "分类层级,0->1级,1->2级,2->3级",required = true)
    @NotNull
    @Range(min = 0,max = 2)
    private Integer level;
}
