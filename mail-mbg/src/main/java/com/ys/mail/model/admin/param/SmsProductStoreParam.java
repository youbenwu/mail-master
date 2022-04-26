package com.ys.mail.model.admin.param;

import com.ys.mail.annotation.EnumContains;
import com.ys.mail.entity.SmsProductStore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 审核用户店铺参数
 *
 * @author CRH
 * @date 2022-04-26 14:21
 * @since 1.0
 */
@Data
@ApiModel(value = "SmsProductStoreParam", description = "审核用户店铺参数")
public class SmsProductStoreParam {

    @ApiModelProperty(value = "用户店铺ID", required = true)
    @Pattern(regexp = "^\\d{19}$")
    private String pdtStoreId;

    @ApiModelProperty(value = "审核状态", required = true)
    @EnumContains(enumClass = SmsProductStore.ReviewState.class, exclude = {"0"})
    private Integer reviewState;

    @NotBlank
    @ApiModelProperty(value = "审核描述")
    private String reviewDesc;
}
