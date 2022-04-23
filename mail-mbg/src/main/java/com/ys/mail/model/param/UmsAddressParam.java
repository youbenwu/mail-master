package com.ys.mail.model.param;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

/**
 * @author DT
 * @version 1.0
 * @date 2021-11-16 09:59
 */
@Data
public class UmsAddressParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "收货地址主键id,新增传0",required = true)
    @NotBlank
    @Pattern(regexp = "^0|^\\d{19}$")
    private String addressId;

    @ApiModelProperty(value = "联系人",required = true)
    @NotBlank
    private String contacts;

    @ApiModelProperty(value = "联系电话",required = true)
    @NotBlank
    @Pattern(regexp = "^[1][3,4,5,6,7,8,9][0-9]{9}$")
    private String phone;

    @ApiModelProperty(value = "一级地址省编码",required = true)
    @NotNull
    private Integer provinceCode;

    @ApiModelProperty(value = "一级地址:省级地址",required = true)
    @NotBlank
    private String province;

    @ApiModelProperty(value = "二级地址:市级地址编码",required = true)
    @NotNull
    private Integer cityCode;

    @ApiModelProperty(value = "二级地址:市级地址",required = true)
    @NotBlank
    private String city;

    @ApiModelProperty(value = "三级地址:县级地址编码",required = true)
    @NotNull
    private Integer countyCode;

    @ApiModelProperty(value = "三级地址:县级地址",required = true)
    @NotBlank
    private String county;

    @ApiModelProperty(value = "详细地址",required = true)
    @NotBlank
    private String clientAddress;

    @ApiModelProperty(value = "是否默认地址0->false,1->true",required = true)
    @NotNull
    @TableField("is_default_status")
    private Boolean defaultStatus;

    @ApiModelProperty(value = "地址标签如0为其它1为家2为公司3学校")
    private Integer label;

}
