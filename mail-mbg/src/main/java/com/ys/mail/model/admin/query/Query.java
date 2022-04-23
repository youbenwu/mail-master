package com.ys.mail.model.admin.query;

import com.ys.mail.annotation.BlankOrPattern;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author DT
 * @version 1.0
 * @date 2021-10-20 15:36
 */
@Data
public class Query implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "当前页", required = true)
    @Range(min = 1, message = "页码最小为1")
    @NotNull(message = "当前页不能为空")
    private Integer pageNum = 1;

    @ApiModelProperty(value = "分页条数", required = true)
    //@Range(min = 1, max = 50, message = "分页条数范围为1-50条")
    //@NotNull(message = "分页范围不能为空")
    private Integer pageSize = 10;

    @ApiModelProperty(value = "查询参数")
    private String keyword;

    @ApiModelProperty(value = "开始时间，格式：yyyy-MM-dd HH:mm:ss")
    @BlankOrPattern(regexp = "^[12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\\s+([0-1]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$", message = "开始时间不合法，请检查！")
    private String beginTime;

    @ApiModelProperty(value = "结束时间，格式：如上")
    @BlankOrPattern(regexp = "^[12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\\s+([0-1]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$", message = "结束时间不合法，请检查！")
    private String endTime;
}
