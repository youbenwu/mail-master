package com.ys.mail.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 合伙人地址信息
 *
 * @author CRH
 * @date 2022-04-24 17:27
 * @since 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "PartnerAddressDTO")
public class PartnerAddressDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "合伙人ID，用于校验地址")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long partnerId;

    @ApiModelProperty(value = "合伙人手机号")
    private String partnerPhone;

    @ApiModelProperty(value = "完整的合伙人地址")
    private String partnerAddress;

}
