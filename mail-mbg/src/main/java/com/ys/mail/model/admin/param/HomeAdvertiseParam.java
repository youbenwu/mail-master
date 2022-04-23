package com.ys.mail.model.admin.param;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author DT
 * @version 1.0
 * @date 2021-11-03 11:00
 */
@Data
public class HomeAdvertiseParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键", required = true)
    @NotNull
    private Long homeAdvId;

    @ApiModelProperty(value = "轮播图名称", required = true)
    @NotBlank
    private String homeAdvName;

    @ApiModelProperty(value = "轮播位置：0->PC首页轮播；1->app首页轮播", required = true)
    @NotNull
    @Range(min = 0, max = 1)
    private Integer homeAdvType;

    @ApiModelProperty(value = "照片", required = true)
    @NotBlank
    private String pic;

    @ApiModelProperty(value = "开启时间", required = true)
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "结束时间", required = true)
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "上下线状态：0->下线；1->上线")
    @TableField("is_home_adv_status")
    @NotNull
    private Boolean homeAdvStatus;

    @ApiModelProperty(value = "链接地址")
    private String url;

    @ApiModelProperty(value = "备注")
    private String note;

    @ApiModelProperty(value = "排序")
    private Integer sort;

}
