package com.ys.mail.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ys.mail.annotation.Sensitive;
import com.ys.mail.enums.EnumSensitiveType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Desc 被邀请成员数据
 * @Author CRH
 * @Create 2022-03-11 19:44
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "UserInviteItemDataVO")
public class UserInviteItemDataVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("被邀请人id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    @ApiModelProperty(value = "被邀请时间")
    private Date inviteTime;

    @ApiModelProperty("被邀请人的电话")
    @Sensitive(type = EnumSensitiveType.PHONE_NUM)
    private String phone;

    @ApiModelProperty("被邀请人的头像，建议压缩显示")
    private String head;

    @ApiModelProperty("被邀请人的团队人数")
    private Integer teamNum;

    @ApiModelProperty("被邀请人的有效消费笔数")
    private Integer consumeNum;

    @ApiModelProperty("被邀请人的有效消费总金额，100=1元")
    private Long consumeTotal;

}
