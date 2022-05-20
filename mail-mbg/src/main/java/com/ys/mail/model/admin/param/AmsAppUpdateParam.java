package com.ys.mail.model.admin.param;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ys.mail.annotation.BlankOrPattern;
import com.ys.mail.enums.RegularEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * APP应用更新参数
 *
 * @author CRH
 * @date 2022-05-16 10:26
 * @since 1.0
 */
@Data
@ApiModel(value = "AmsAppUpdateParam", description = "APP应用更新参数")
public class AmsAppUpdateParam {

    @NotNull
    @ApiModelProperty(value = "APP主键ID")
    @BlankOrPattern(regEnum = RegularEnum.KEY)
    private Long id;

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
    @Pattern(regexp = "[a-zA-Z\\d]+", message = "：名称格式错误，只支持字母、数字")
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
