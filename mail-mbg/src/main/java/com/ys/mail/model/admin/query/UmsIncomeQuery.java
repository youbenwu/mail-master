package com.ys.mail.model.admin.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @Desc 订单查询对象
 * @Author CRH
 * @Create 2021-12-27 13:48
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UmsIncomeQuery extends Query {

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "支付宝订单号")
    private String incomeNo;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "是否升序：0->降序，1->升序", required = true)
    @NotBlank
    @Pattern(regexp = "^[01]$", message = "参数范围不正确")
    private String asc;

}


