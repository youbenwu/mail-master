package com.ys.mail.model.admin.vo.statistics;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Desc 用户相关数据统计
 * @Author CRH
 * @Create 2022-03-03 10:11
 */
@Data
@ApiModel(value = "UserCollectDataVo")
public class UserCollectDataVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户总人数")
    private Integer userNumber;

    @ApiModelProperty(value = "高级用户人数")
    private Integer seniorUser;

    @ApiModelProperty(value = "合伙人总人数")
    private Integer partnerNumber;

}
