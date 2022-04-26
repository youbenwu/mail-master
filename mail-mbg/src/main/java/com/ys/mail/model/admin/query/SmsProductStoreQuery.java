package com.ys.mail.model.admin.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品店铺查询对象
 *
 * @author CRH
 * @date 2022-04-26 13:28
 * @since 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SmsProductStoreQuery extends Query {

    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    @ApiModelProperty(value = "店铺手机号")
    private String storePhone;

    @ApiModelProperty(value = "店铺审核状态")
    private Long reviewState;

}


