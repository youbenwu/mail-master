package com.ys.mail.model.query;

import com.ys.mail.annotation.DateValidator;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.RegularUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

/**
 * 用一句简单的话来描述下该类
 *
 * @author CRH
 * @date 2022-06-14 15:52
 * @since 1.0
 */
@Data
public class IncomeDimensionQuery {

    @DateValidator(dft = DateValidator.DFT.PATTERN, pattern = RegularUtil.DATE_REGEX)
    @ApiModelProperty(value = "开始时间，格式：支持 年、年月、年月日，如2022、202203、20220301")
    private String beginTime;

    @ApiModelProperty(value = "结束时间，格式同上")
    @DateValidator(dft = DateValidator.DFT.PATTERN, pattern = RegularUtil.DATE_REGEX)
    private String endTime;

    @ApiModelProperty(value = "最后一条记录ID，用于分页")
    private Long lastIncomeId;

    @ApiModelProperty(value = "是否开启按最近时间查询，0->不开启；1->开启，默认开启")
    private String lately = "1";

    @Range(min = 1, max = 50, message = "分页大小范围为1-50条")
    @ApiModelProperty(value = "分页条数，默认10条，最大50条")
    private String pageSize = "10";

    public void setPageSize(String pageSize) {
        if (BlankUtil.isNotEmpty(pageSize)) {
            this.pageSize = pageSize;
        }
    }

    public void setLately(String lately) {
        if (BlankUtil.isNotEmpty(lately)) {
            this.lately = lately;
        }
    }
}
