package com.ys.mail.model.admin.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @Desc 表单提交参数
 * @Author CRH
 * @Create 2022-02-14 15:09
 */
@Data
@ApiModel(value = "SysSettingParam", description = "系统设置参数")
public class SysSettingParam {

    @ApiModelProperty(value = "主键ID：0->表示新增，其他则表示修改", required = true)
    @NotNull
    @NotBlank
    @Pattern(regexp = "^0|^\\d{19}$")
    private String sysSettingId;

    @ApiModelProperty(value = "设置类型，不能重复，添加之后不能变更，-1->表示自动填充，0或以上表示按需定义，目前可添加范围在0~100.可以扩展", required = true)
    @Pattern(regexp = "^(-1)$|^\\d+$")
    @NotNull
    @NotBlank
    private String settingType;

    @ApiModelProperty(value = "值类型，添加之后不能变更，修改时将会检查类型是否匹配，类型为：STRING, INTEGER, DOUBLE, BOOLEAN, JSON, LIST", required = true)
    @NotNull
    @NotBlank
    @Length(max = 20)
    private String settingValueType;

    @ApiModelProperty(value = "表单类型，添加之后不能变更，用于前端区分组件，根据内容决定，类型为：TEXT、TEXTAREA、BOOLEAN、NUMBER、DOUBLE、DATE、TIME、DATETIME、IMAGE、IMAGES、JSON", required = true)
    @NotNull
    @NotBlank
    @Length(max = 20)
    private String settingFormType;

    @ApiModelProperty(value = "设置名称，表示该项设置的简短含义，用于显示，如：系统设置、APP设置、第三方设置等", required = true)
    @Length(max = 20)
    @NotNull
    @NotBlank
    private String settingKey;

    @ApiModelProperty(value = "分组名称，分组时用于显示，可选，可自定义")
    @Length(max = 30)
    private String settingGroupName;

    @ApiModelProperty(value = "设置值，表示该项设置的值，统一存储为字符串，使用时按类型解析，修改时将会检查类型是否匹配")
    private String settingValue;

    @ApiModelProperty(value = "设置默认值，作为参考与备用，添加之后不能变更；另外当该值不传时，将与设置值相同，而使用时当value为空时，使用该值")
    private String settingDefaultValue;

    @ApiModelProperty(value = "设置备注，用于详细描述该设置的作用以及范围等")
    private String settingRemark;

    @ApiModelProperty(value = "是否启用，类型为：false->禁用,true->启用")
    private Boolean enable;

    @ApiModelProperty(value = "是否允许禁用，添加之后不能变更，部分设置不能被禁用，类型为：false->不允许,true->允许")
    private Boolean allowDisable;

    @ApiModelProperty(value = "是否允许修改，添加之后不能变更，部分设置不能被修改：false->不允许,true->允许")
    private Boolean allowUpdate;

    @ApiModelProperty(value = "是否允许删除，添加之后不能变更，部分设置不能被删除：false->不允许,true->允许")
    private Boolean allowDelete;

}
