package com.ys.mail.model.admin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author DT
 * @version 1.0
 * @date 2021-10-20 15:03
 */
@Data
@Builder
@ApiModel(value = "UserImInfoVO", description = "用户通信信息对象")
public class UserImInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "头像")
    private String headPortrait;
}
