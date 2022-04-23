package com.ys.mail.model.admin.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author DT
 * @version 1.0
 * @date 2021-10-21 10:27
 */
@Data
@ApiModel(value = "PcRoleParam", description = "后台角色参数")
public class PcRoleParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "角色id",required = true)
    @NotNull
    private Long roleId;

    @ApiModelProperty(value = "角色名称",required = true)
    @NotBlank
    @Size(min = 4,message = "长度不能低于4")
    private String roleName;

    @ApiModelProperty(value = "角色描述",required = true)
    @NotBlank
    private String roleRemark;
}
