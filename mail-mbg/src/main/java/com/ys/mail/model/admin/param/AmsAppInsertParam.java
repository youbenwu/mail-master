package com.ys.mail.model.admin.param;

import com.ys.mail.annotation.BlankOrPattern;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * APP应用添加参数
 *
 * @author CRH
 * @date 2022-05-16 10:26
 * @since 1.0
 */
@Data
@ApiModel(value = "AmsAppInsertParam", description = "APP应用添加参数")
public class AmsAppInsertParam {

    @NotBlank
    @Length(max = 20)
    @ApiModelProperty(value = "APP名称", required = true)
    private String name;

    @NotBlank
    @ApiModelProperty(value = "APP存储路径，需要使用异步文件上传获取", required = true)
    @Pattern(regexp = "^/[a-zA-Z\\d]+/[a-zA-Z\\d_.-]+\\.apk$", message = "：请输入正确的路径，以(.apk)结尾")
    private String url;

    @NotNull
    @ApiModelProperty(value = "APP类型，0->APP1，1->APP2", required = true)
    @BlankOrPattern(regexp = "^[01]$", message = "：请输入正确的类型")
    private Integer type;

    @NotNull
    @ApiModelProperty(value = "APP版本号", required = true)
    private String versionCode;

    @NotNull
    @Length(max = 50)
    @ApiModelProperty(value = "APP版本名称", required = true)
    private String versionName;

    @NotNull
    @ApiModelProperty(value = "是否使用Logo，0->不使用，1->使用", required = true)
    private Boolean useLogo;

    @NotBlank
    @Length(max = 20)
    @ApiModelProperty(value = "更新标题", required = true)
    private String updateTitle;

    @NotBlank
    @ApiModelProperty(value = "更新内容", required = true)
    private String updateContent;

    @NotNull
    @ApiModelProperty(value = "是否强制更新，0->不强制，1->强制", required = true)
    private Boolean forcedUpdate;

    @ApiModelProperty(value = "备注")
    private String remark;

}
