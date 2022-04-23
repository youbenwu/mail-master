package com.ys.mail.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ys.mail.annotation.Sensitive;
import com.ys.mail.enums.EnumSensitiveType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel("被邀请人的item对象")
public class UserInviteItemVO implements Serializable {

    @ApiModelProperty("主键")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty("被邀请人id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    @ApiModelProperty(value = "邀请时间")
    private Date createTime;

    @ApiModelProperty("被邀请人的电话")
    @Sensitive(type = EnumSensitiveType.PHONE_NUM)
    private String phone;

    @ApiModelProperty("被邀请人的头像")
    private String head;

    @ApiModelProperty("被邀请人的社团人数")
    private Integer teamNum;

    @ApiModelProperty("被邀请人的消费记录数")
    private Integer consumeNum;

    @ApiModelProperty("被邀请人的消费总金额")
    private String consumeTotal;

    @ApiModelProperty("被邀请人的状态，暂时不用")
    private Integer status = 1;
}
