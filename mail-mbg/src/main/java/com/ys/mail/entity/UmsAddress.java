package com.ys.mail.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户收货地址表
 * </p>
 *
 * @author 070
 * @since 2021-11-15
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="UmsAddress对象", description="用户收货地址表")
public class UmsAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "收货地址主键id")
    @TableId(value = "address_id", type = IdType.INPUT)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long addressId;

    @ApiModelProperty(value = "用户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    @ApiModelProperty(value = "联系人")
    private String contacts;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "一级地址省编码")
    private Integer provinceCode;

    @ApiModelProperty(value = "一级地址:省级地址")
    private String province;

    @ApiModelProperty(value = "二级地址:市级地址编码")
    private Integer cityCode;

    @ApiModelProperty(value = "二级地址:市级地址")
    private String city;

    @ApiModelProperty(value = "三级地址:县级地址编码")
    private Integer countyCode;

    @ApiModelProperty(value = "三级地址:县级地址")
    private String county;

    @ApiModelProperty(value = "详细地址")
    private String clientAddress;

    @ApiModelProperty(value = "是否默认地址0->false,1->true")
    @TableField("is_default_status")
    private Boolean defaultStatus;

    @ApiModelProperty(value = "地址标签如0为其它1为家2为公司3学校")
    private Integer label;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date updateTime;


}
