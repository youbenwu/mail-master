package com.ys.mail.model.admin.vo;

import com.baomidou.mybatisplus.annotation.TableField;
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
    private String version;

    @ApiModelProperty(value = "APP相对存储地址")
    private String url;

    @ApiModelProperty(value = "APP文件大小，单位字节")
    private Long size;

    @ApiModelProperty(value = "APP文件上传状态：0->未上传，1->已上传")
    private Integer uploadStatus;

    @ApiModelProperty(value = "二维码相对存储地址")
    private String qrcodeUrl;

    @ApiModelProperty(value = "二维码名称")
    private String qrcodeName;

    @ApiModelProperty(value = "内嵌Logo类型，0->APP1,1->APP2")
    private Integer logoType;

    @ApiModelProperty(value = "是否使用内嵌Logo，false->不使用,true->使用")
    private Boolean useLogo;

    @ApiModelProperty(value = "注册时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "修改人名称")
    private String username;

}
