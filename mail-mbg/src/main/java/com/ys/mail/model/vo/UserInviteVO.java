package com.ys.mail.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel("我的邀请详情")
public class UserInviteVO implements Serializable {

    @ApiModelProperty("总收益")
    private Long total;

    @ApiModelProperty("佣金奖励")
    private Long commission;

    @ApiModelProperty("社团奖励")
    private Long team;

    @ApiModelProperty("当前用户的社团人数")
    private Integer teamNum;

    @ApiModelProperty("文本说明")
    private String levelTip;

    @ApiModelProperty("被邀请人的列表")
    private List<UserInviteItemVO> invite;
}
