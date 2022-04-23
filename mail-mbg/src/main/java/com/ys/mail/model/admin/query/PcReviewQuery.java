package com.ys.mail.model.admin.query;

import com.ys.mail.annotation.BlankOrPattern;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Desc 订单查询对象
 * @Author CRH
 * @Create 2021-12-27 13:48
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PcReviewQuery extends Query {

    @ApiModelProperty(value = "支付宝账号")
    private String alipayAccount;

    @ApiModelProperty(value = "支付宝名字")
    private String alipayName;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "提现流水号")
    @BlankOrPattern(regexp = "^\\d{19}$", message = "提现流水号不合法，请检查！")
    private String incomeId;

}


