package com.ys.mail.model.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author DT
 * @version 1.0
 * @date 2021-11-09 15:23
 */
@Data
public class OauthParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "客户端token",required = true)
    @NotBlank
    private String token;

    @ApiModelProperty(value = "客户端返回的运营商token",required = true)
    @NotBlank
    private String opToken;

    @ApiModelProperty(value = "客户端返回的运营商，CMCC:中国移动通信, CUCC:中国联通通讯, CTCC:中国电信",required = true)
    @NotBlank
    @Pattern(regexp = "^CMCC|^CUCC|^CTCC")
    private String operator;

    @ApiModelProperty(value = "手机类型,0为苹果,1为安卓",required = true)
    @NotNull
    @Range(min = 0,max = 1)
    private Integer phoneType;

}
