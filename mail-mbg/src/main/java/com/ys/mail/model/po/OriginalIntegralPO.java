package com.ys.mail.model.po;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 本金、积分对象
 *
 * @author CRH
 * @date 2022-06-10 18:24
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OriginalIntegralPO {

    @ApiModelProperty(value = "原本金，1元就是100")
    private Long original;

    @ApiModelProperty(value = "收益积分，1元就是100")
    private Long integral;

    @ApiModelProperty(value = "是否已覆盖过")
    private Boolean cover;
}
