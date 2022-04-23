package com.ys.mail.model.admin.param;

import com.ys.mail.annotation.BlankOrPattern;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.boot.context.properties.bind.DefaultValue;

import javax.validation.constraints.Pattern;

/**
 * @Desc 导出订单对象
 * @Author CRH
 * @Create 2022-01-05 21:03
 */
@Data
@ApiModel(value = "ExportOrderParam", description = "导出订单对象")
public class ExportOrderParam {

    @ApiModelProperty(value = "时间类型：1-当月，2-近三个月，3-近半年，0-表示自选时间段（beginTime-endTime）")
    @Pattern(regexp = "^[0-4]$")
    private String timeType;

    @ApiModelProperty(value = "当timeType为0时生效，开始时间(下单时间)，格式：yyyy-MM-dd HH:mm:ss")
    @BlankOrPattern(regexp = "^[12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\\s+([0-1]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$", message = "开始时间格式不合法，请检查！")
    private String beginTime;

    @ApiModelProperty(value = "当timeType为0时生效，结束时间，格式：yyyy-MM-dd HH:mm:ss")
    @BlankOrPattern(regexp = "^[12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\\s+([0-1]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$", message = "结束时间格式不合法，请检查！")
    private String endTime;

    @ApiModelProperty(value = "是否导出订单详情导出，默认false")
    private Boolean openOrderItem = false;

    @ApiModelProperty(value = "删除状态：0->未删除；1->已删除，默认false")
    private Boolean deleteStatus = false;

    // TODO:暂时只添加以下过滤条件
    @ApiModelProperty(value = "用户ID，支持任意模糊匹配")
    @BlankOrPattern(regexp = "^\\d{19}$", message = "用户ID不合法，请检查！")
    private String userId;

}
