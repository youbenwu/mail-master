package com.ys.mail.model.admin.vo;

import com.ys.mail.entity.SmsProductStore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 商品店铺信息
 *
 * @author CRH
 * @date 2022-04-26 13:28
 * @since 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "SmsProductStoreVO", description = "商品店铺信息")
public class SmsProductStoreVO extends SmsProductStore implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "审核人名称")
    private String reviewName;

}
