package com.ys.mail.model.admin.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;

/**
 * @author 24
 * @date 2022/1/25 16:30
 * @description
 */
@Data
public class QueryParentQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "当前页", required = true)
    @Range(min = 1, message = "页码最小为1")
    private Integer pageNum = 1;

    @ApiModelProperty(value = "分页条数", required = true)
//    @Range(min = 1, max = 50, message = "分页条数范围为1-50条")
    private Integer pageSize = 10;

    @ApiModelProperty(value = "公司名称")
    private String corporateName;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "身份证号")
    private String idyCard;

    @ApiModelProperty(value = "审核状态")
    private String authStatus;

    @ApiModelProperty(value = "开始时间")
    private String createTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

}
