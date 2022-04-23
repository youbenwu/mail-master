package com.ys.mail.model.param;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @date 2021-11-22 13:18
 */
@Data
public class IdyAuthParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id,审核失败的时候传id修改",required = true)
    @NotNull
    private Long idyAuthId;

    @ApiModelProperty(value = "真实姓名",required = true)
    @NotBlank
    private String realName;

    @ApiModelProperty(value = "身份证号",required = true)
    @NotBlank
    @Size(min = 15,max = 18)
    @Pattern(regexp = "(^\\d{8}(0\\d|10|11|12)([0-2]\\d|30|31)\\d{3}$)|(^\\d{6}(18|19|20)\\d{2}(0\\d|10|11|12)([0-2]\\d|30|31)\\d{3}(\\d|X|x)$)",message = "请输入正确的身份证号")
    private String idyCard;

    @ApiModelProperty(value = "身份证正面照片",required = true)
    @NotBlank
    private String idyFront;

    @ApiModelProperty(value = "身份证反面照片",required = true)
    @NotBlank
    private String idyReverse;

}
