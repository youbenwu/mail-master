package com.ys.mail.model.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 腾讯人脸识别主要参数
 * @author DT
 * @version 1.0
 * @date 2022-01-24 10:40
 */
@Data
public class VerifyFaceParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "调用方式:0->轻创营,1->卖乐吧,可以不传,不传默认调用轻创营")
    @Range(min = 0,max = 1)
    private Integer cpyType;

    @ApiModelProperty(value = "身份证号",required = true)
    @NotBlank
    @Pattern(regexp = "(^\\d{8}(0\\d|10|11|12)([0-2]\\d|30|31)\\d{3}$)|(^\\d{6}(18|19|20)\\d{2}(0\\d|10|11|12)([0-2]\\d|30|31)\\d{3}(\\d|X|x)$)",message = "请输入正确的身份证号")
    private String idCardNo;

    @ApiModelProperty(value = "姓名",required = true)
    @NotBlank
    private String name;
}
