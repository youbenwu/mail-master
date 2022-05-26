package com.ys.mail.model.admin.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

import java.io.Serializable;
import java.util.Date;

/**
 * APP发布信息-数据传输对象
 *
 * @author CRH
 * @date 2022-05-23 17:22
 * @since 1.0
 */
@Data
@Builder
public class AppReleaseInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "APPID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty(value = "APP名称")
    private String appName;

    @ApiModelProperty(value = "APP版本号")
    private Integer versionCode;

    @ApiModelProperty(value = "APP版本名称")
    private String versionName;

    @ApiModelProperty(value = "完整的二维码存储路径")
    private String fullQrcodeUrl;

    @ApiModelProperty(value = "更新标题")
    private String updateTitle;

    @ApiModelProperty(value = "更新内容")
    private String updateContent;

    @ApiModelProperty(value = "是否强制更新，0->不强制，1->强制")
    private Boolean forcedUpdate;

    @ApiModelProperty(value = "APP下载链接")
    private String downloadUrl;

    @ApiModelProperty(value = "APP大小，单位字节")
    private Long packageSize;

    @ApiModelProperty(value = "发布时间")
    private Date updateTime;

}
