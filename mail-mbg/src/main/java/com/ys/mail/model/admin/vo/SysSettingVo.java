package com.ys.mail.model.admin.vo;

import com.ys.mail.entity.SysSetting;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Desc
 * @Author CRH
 * @Create 2022-02-14 17:44
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysSettingVo extends SysSetting implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "头像")
    private String headPortrait;

    @ApiModelProperty(notes = "设置值，返回实际类型，方便数据处理")
    private Object value;

    @ApiModelProperty(notes = "设置默认值，返回实际类型，方便数据处理")
    private Object defaultValue;
}
