package com.ys.mail.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * APP应用
 * </p>
 *
 * @author 070
 * @since 2022-05-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "AmsApp对象", description = "AmsApp对象")
public class AmsApp implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "APP主键ID")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @ApiModelProperty(value = "APP名称")
    private String name;

    @ApiModelProperty(value = "APP版本号")
    @TableField(fill = FieldFill.INSERT)
    private String version;

    @ApiModelProperty(value = "APP相对存储地址")
    private String url;

    @ApiModelProperty(value = "APP文件大小")
    private Long size;

    @ApiModelProperty(value = "APP文件上传状态：0->未上传，1->已上传")
    private Integer uploadStatus;

    @ApiModelProperty(value = "二维码相对存储地址")
    private String qrcodeUrl;

    @ApiModelProperty(value = "二维码名称")
    private String qrcodeName;

    @ApiModelProperty(value = "内嵌Logo类型，0->APP1,1->APP2")
    private Integer logoType;

    @ApiModelProperty(value = "是否使用内嵌Logo，0->不使用,1->使用")
    @TableField("is_use_logo")
    private Boolean useLogo;

    @ApiModelProperty(value = "修改人ID")
    private Long pcUserId;

    @ApiModelProperty(value = "注册时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

}
