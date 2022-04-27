package com.ys.mail.model.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用一句简单的话来描述下该类
 *
 * @author CRH
 * @date 2022-04-27 15:19
 * @since 1.0
 */
@Data
public class ProductStoreObjDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品店铺id")
    private Long pdtStoreId;

    @ApiModelProperty(value = "店铺Logo")
    private String storeLogo;

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
    private Date updateTime;
}
