package com.ys.mail.model.param;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * @author DT
 * @version 1.0
 * @date 2021-11-22 14:37
 */
@Data
public class UmsBankParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "银行卡名称",required = true)
    @NotBlank
    private String bankName;

    @ApiModelProperty(value = "银行卡编码",required = true)
    @NotBlank
    private String bankCode;

    @ApiModelProperty(value = "银行卡卡号",required = true)
    @NotBlank
    @Pattern(regexp = "^[1-9]\\d{9,29}$")
    private String bankCard;

    @ApiModelProperty(value = "所在地",required = true)
    @NotBlank
    private String bankAddress;

    @ApiModelProperty(value = "绑定银行卡的手机号",required = true)
    @NotBlank
    @Pattern(regexp = "^[1][3,4,5,6,7,8,9][0-9]{9}$")
    private String bankPhone;

    @ApiModelProperty(value = "验证码",required = true)
    @NotBlank
    @Size(min = 6)
    private String authCode;

}
