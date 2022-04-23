package com.ys.mail.model.admin.vo.statistics;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ys.mail.annotation.Sensitive;
import com.ys.mail.enums.EnumSensitiveType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Desc 用户邀请天梯榜单
 * @Author CRH
 * @Create 2022-03-10 11:17
 */
@Data
@ApiModel(value = "InviteLadderListVo", description = "用户邀请天梯榜单")
public class InviteLadderListVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id，邀请人")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    @ApiModelProperty(value = "邀请人昵称，默认选择")
    private String nickname;

    @ApiModelProperty(value = "邀请人头像")
    private String headPortrait;

    @ApiModelProperty(value = "邀请人支付宝姓名，首选")
    private String alipayName;

    @ApiModelProperty(value = "下级邀请数量")
    private Integer inviteCount;
}
