package com.ys.mail.model.admin.vo;

import com.ys.mail.entity.OmsOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * --
 *
 * @author CRH
 * @date 2022-04-19 15:19
 * @since 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PcUserOrderVO extends OmsOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "手机号")
    private String phone;

}
