package com.ys.mail.model.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 商品店铺信息
 *
 * @author CRH
 * @date 2022-04-26 13:28
 * @since 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "ProductStoreVO", description = "商品店铺信息")
public class ProductStoreVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品店铺id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long pdtStoreId;

    @ApiModelProperty(value = "用户id，商品售卖人，店铺所属用户")
    private Long userId;

    // @ApiModelProperty(value = "店铺Logo")
    // private String storeLogo;

    @ApiModelProperty(value = "店铺名称，自定义")
    private String storeName;

    @ApiModelProperty(value = "店主名称，用户名称")
    private String storeBoss;

    @ApiModelProperty(value = "店主手机，用户手机")
    private String storePhone;

    @ApiModelProperty(value = "店主经营地址")
    private String storeAddress;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Date updateTime;

    @ApiModelProperty(value = "审核状态：0->待审核，1->已通过，2->不通过")
    private Integer reviewState;

    @ApiModelProperty(value = "审核描述")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String reviewDesc;

}
