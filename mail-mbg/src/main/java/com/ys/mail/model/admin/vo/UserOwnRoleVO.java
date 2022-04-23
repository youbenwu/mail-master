package com.ys.mail.model.admin.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ys.mail.entity.PcRole;
import com.ys.mail.model.admin.tree.PcMenuTree;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author DT
 * @version 1.0
 * @date 2021-11-22 17:51
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserOwnRoleVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("当前用户所拥有的角色")
    private String[] arr;

    @ApiModelProperty(value = "所有的角色集合")
    private List<PcRole> roles;
}
