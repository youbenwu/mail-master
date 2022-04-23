package com.ys.mail.model.admin.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author DT
 * @version 1.0
 * @date 2021-11-03 13:14
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HomeAdvertiseQuery extends Query implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "轮播位置：0->PC首页轮播；1->app首页轮播")
    private Integer homeAdvType;

}
