package com.ys.mail.model.admin.dto;

import com.ys.mail.entity.PmsPartnerRe;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author ghdhj
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PcPartnerReDTO extends PmsPartnerRe implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户对象")
    private User user;

    @Data
    static class User {

        @ApiModelProperty(value = "支付宝真实姓名")
        private String alipayName;
    }

}
