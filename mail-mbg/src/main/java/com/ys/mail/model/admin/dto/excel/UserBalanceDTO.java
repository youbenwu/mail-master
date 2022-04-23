package com.ys.mail.model.admin.dto.excel;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Desc 用户余额数据
 * @Author CRH
 * @Create 2022-03-30 16:44
 */
@Data
@ApiModel(value = "UserBalanceDTO")
public class UserBalanceDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    @ApiModelProperty(value = "用户最新余额")
    private Double currentBalance;
}
