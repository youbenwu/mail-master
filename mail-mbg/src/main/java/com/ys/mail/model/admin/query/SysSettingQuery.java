package com.ys.mail.model.admin.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Desc
 * @Author CRH
 * @Create 2022-02-14 15:08
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysSettingQuery extends Query {

    @ApiModelProperty(value = "分组名称，分组时用于显示，可选，可自定义")
    private String settingGroupName;

    @ApiModelProperty(value = "设置类型，主要根据该类型确定唯一设置，不能重复")
    private Integer settingType;

    @ApiModelProperty(value = "设置名称，表示该项设置的简短含义，用于显示，如：系统设置、APP设置、第三方设置等")
    private String settingKey;

    @ApiModelProperty(value = "是否启用，类型为：false->禁用,true->启用")
    private Boolean enable;

    @ApiModelProperty(value = "是否允许禁用，部分设置不能被禁用，类型为：false->不允许,true->允许")
    private Boolean allowDisable;

    @ApiModelProperty(value = "是否允许删除：false->不允许,true->允许")
    private Boolean allowDelete;

}
