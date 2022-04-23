package com.ys.mail.model.admin.vo;

import com.ys.mail.entity.OmsOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Desc
 * @Author CRH
 * @Create 2022-02-23 15:12
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
