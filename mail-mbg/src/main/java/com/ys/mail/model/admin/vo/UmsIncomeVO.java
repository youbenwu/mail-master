package com.ys.mail.model.admin.vo;

import com.ys.mail.annotation.Sensitive;
import com.ys.mail.entity.UmsIncome;
import com.ys.mail.enums.EnumSensitiveType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Desc 用户流水显示对象
 * @Author CRH
 * @Create 2021-12-29 20:01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "UmsIncomeVO", description = "用户流水显示对象")
public class UmsIncomeVO extends UmsIncome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Sensitive(type = EnumSensitiveType.NAME)
    @ApiModelProperty(value = "支付宝姓名")
    private String alipayName;

    @Sensitive(type = EnumSensitiveType.PHONE_NUM)
    @ApiModelProperty(value = "手机号码")
    private String phone;

}
