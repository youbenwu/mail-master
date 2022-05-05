package com.ys.mail.model.query;

import com.ys.mail.util.BlankUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 分页查询对象
 *
 * @author CRH
 * @date 2022-05-05 13:40
 * @since 1.0
 */
@Getter
@ApiModel(value = "PageQuery", description = "分页查询对象")
public class PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "当前页不能为空")
    @ApiModelProperty(value = "当前页，默认为1")
    private Integer pageNum = 1;

    @NotNull(message = "分页条数不能为空")
    @ApiModelProperty(value = "分页条数，默认10条")
    private Integer pageSize = 10;

    public void setPageNum(Integer pageNum) {
        if (BlankUtil.isNotEmpty(pageNum)) {
            this.pageNum = pageNum;
        }
    }

    public void setPageSize(Integer pageSize) {
        if (BlankUtil.isNotEmpty(pageSize)) {
            this.pageSize = pageSize;
        }
    }
}
