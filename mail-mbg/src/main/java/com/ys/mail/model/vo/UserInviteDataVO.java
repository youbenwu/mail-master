package com.ys.mail.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Desc 用户邀请数据汇总
 * @Author CRH
 * @Create 2022-03-11 19:41
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "UserInviteDataVO")
public class UserInviteDataVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("团队人数")
    private Integer teamNum;

    @ApiModelProperty("团队总收益，100=1元")
    private Long teamSum;

    @ApiModelProperty("成员的有效订单总笔数，当月数据")
    private Integer consumeNum;

    @ApiModelProperty("成员的有效订单总金额，100=1元，当月数据")
    private Long consumeTotal;

    @ApiModelProperty("被邀请人的列表")
    private List<UserInviteItemDataVO> invite;

}
