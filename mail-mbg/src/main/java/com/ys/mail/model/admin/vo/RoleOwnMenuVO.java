package com.ys.mail.model.admin.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
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
 * @date 2021-11-22 15:58
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RoleOwnMenuVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("当前角色所拥有的菜单数组")
    private String[] arr;

    @ApiModelProperty(value = "树集合")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<PcMenuTree> children;
}
