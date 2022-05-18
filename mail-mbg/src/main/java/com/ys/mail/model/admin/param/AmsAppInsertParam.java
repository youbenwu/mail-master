package com.ys.mail.model.admin.param;

import com.ys.mail.annotation.BlankOrPattern;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Digits;
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
    @ApiModelProperty(value = "APP名称")
    private String name;

    @NotNull
    @ApiModelProperty(value = "APP版本号")
    @Digits(integer = 10, fraction = 3, message = "：请输入正确的数值(最多3位小数点)")
    private String version;

    @NotBlank
    @ApiModelProperty(value = "APP相对存储地址")
    @Pattern(regexp = "^/[a-zA-Z\\d]+/[a-zA-Z\\d_.-]+\\.apk$", message = "：请输入正确的相对路径，以(.apk)结尾")
    private String url;

    @NotBlank
    @Pattern(regexp = "[a-zA-Z]+", message = "：名称格式错误")
    @ApiModelProperty(value = "二维码名称")
    private String qrcodeName;

    @NotNull
    @ApiModelProperty(value = "内嵌Logo类型，0->APP1,1->APP2")
    @BlankOrPattern(regexp = "^[01]$", message = "：请输入正确的类型")
    private Integer logoType;

    @NotNull
    @ApiModelProperty(value = "是否使用内嵌Logo")
    private Boolean useLogo;

}
