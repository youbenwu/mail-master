package com.ys.mail.model.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author DT
 * @version 1.0
 * @date 2021-12-29 15:29
 */
@Data
public class QueryObject implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "当前页",required = true)
    @NotNull(message = "当前页不能为空")
    private Integer pageNum = 1;

    @ApiModelProperty(value = "分页条数",required = true)
    @NotNull(message = "分页条数不能为空")
    private Integer pageSize = 10;

    @ApiModelProperty(value = "查询参数")
    private String keyword;
}
