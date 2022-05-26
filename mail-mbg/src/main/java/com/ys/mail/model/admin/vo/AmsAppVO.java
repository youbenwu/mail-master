package com.ys.mail.model.admin.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用一句简单的话来描述下该类
 *
 * @author CRH
 * @date 2022-05-16 18:29
 * @since 1.0
 */
@Data
public class AmsAppVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "APP主键ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty(value = "APP名称")
    private String name;

    @ApiModelProperty(value = "APP版本号")
    private String versionCode;

    @ApiModelProperty(value = "APP版本名称")
    private String versionName;

    @ApiModelProperty(value = "APP路径")
    private String url;

    @ApiModelProperty(value = "APP文件大小，单位字节")
    private Long size;

    @ApiModelProperty(value = "APP类型，0->APP1,1->APP2")
    private Integer type;

    @ApiModelProperty(value = "APP文件上传状态：0->未上传，1->已上传")
    private Integer uploadStatus;

    @ApiModelProperty(value = "二维码路径")
    private String qrcodeUrl;

    @ApiModelProperty(value = "是否使用Logo，0->不使用，1->使用")
    private Boolean useLogo;

    @ApiModelProperty(value = "更新标题")
    private String updateTitle;

    @ApiModelProperty(value = "更新内容")
    private String updateContent;

    @ApiModelProperty(value = "是否强制更新，false->不强制,true->强制")
    private Boolean forcedUpdate;

    @ApiModelProperty(value = "是否已发布，false-未发布，true->已发布")
    private Boolean released;

    @ApiModelProperty(value = "修改人名称")
    private String username;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "注册时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

}
