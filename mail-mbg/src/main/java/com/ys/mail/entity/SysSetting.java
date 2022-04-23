package com.ys.mail.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 全局设置表（可以新增，但只允许修改部分字段），
 * TODO：新的设置表（后面的所有设置统一迁移到该表）
 * </p>
 *
 * @author 007
 * @since 2022-02-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "SysSetting对象", description = "全局设置表（可以新增，但只允许修改部分字段）")
public class SysSetting implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键，系统设置ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "sys_setting_id", type = IdType.INPUT)
    private Long sysSettingId;

    @ApiModelProperty(value = "分组名称，分组时用于显示，可选，可自定义")
    private String settingGroupName;

    @ApiModelProperty(value = "设置类型，主要根据该类型确定唯一设置，不能重复，不能变更")
    private Integer settingType;

    @ApiModelProperty(value = "设置名称，表示该项设置的简短含义，用于显示，如：系统设置、APP设置、第三方设置等")
    private String settingKey;

    @ApiModelProperty(value = "设置值，表示该项设置的值，统一存储为字符串，使用时按类型解析")
    private String settingValue;

    @ApiModelProperty(value = "设置默认值，作为参考与备用，不允许修改，当value为空时，使用该值")
    private String settingDefaultValue;

    @ApiModelProperty(value = "值类型，类型为：STRING, INTEGER, DOUBLE, BOOLEAN, JSON, LIST")
    private String settingValueType;

    @ApiModelProperty(value = "表单类型，用于前端区分组件，根据内容决定，类型为：TEXT、TEXTAREA、BOOLEAN、NUMBER、DOUBLE、DATE、TIME、DATETIME、IMAGE、IMAGES、JSON")
    private String settingFormType;

    @ApiModelProperty(value = "设置备注，用于详细描述该设置的作用以及范围等")
    private String settingRemark;

    @ApiModelProperty(value = "是否启用，类型为：0->禁用,1->启用")
    @TableField("is_enable")
    private Boolean enable;

    @ApiModelProperty(value = "是否允许禁用，部分设置不能被禁用，类型为：0->不允许,1->允许")
    @TableField("is_allow_disable")
    private Boolean allowDisable;

    @ApiModelProperty(value = "是否允许修改，部分设置不能被修改：0->不允许,1->允许")
    @TableField("is_allow_update")
    private Boolean allowUpdate;

    @ApiModelProperty(value = "是否允许删除，部分设置不能被删除：0->不允许,1->允许")
    @TableField("is_allow_delete")
    private Boolean allowDelete;

    @ApiModelProperty(value = "操作者用户ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long pcUserId;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

}
