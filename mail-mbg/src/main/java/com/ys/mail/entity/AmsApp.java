package com.ys.mail.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * <p>
 * APP应用
 * </p>
 *
 * @author 070
 * @since 2022-05-13
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "AmsApp对象", description = "AmsApp对象")
public class AmsApp extends BaseEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "APP名称")
    private String name;

    @ApiModelProperty(value = "APP存储路径")
    private String url;

    @ApiModelProperty(value = "APP文件大小")
    private Long size;

    @ApiModelProperty(value = "APP类型，0->APP1，1->APP2")
    private Integer type;

    @ApiModelProperty(value = "APP版本号")
    private String versionCode;

    @ApiModelProperty(value = "APP版本名称")
    private String versionName;

    @ApiModelProperty(value = "APP文件上传状态：0->未上传，1->已上传")
    private Integer uploadStatus;

    @ApiModelProperty(value = "二维码存储路径")
    private String qrcodeUrl;

    @ApiModelProperty(value = "是否使用Logo，0->不使用，1->使用")
    @TableField("is_use_logo")
    private Boolean useLogo;

    @ApiModelProperty(value = "更新标题")
    private String updateTitle;

    @ApiModelProperty(value = "更新内容")
    private String updateContent;

    @ApiModelProperty(value = "是否强制更新，0->不强制，1->强制")
    @TableField("is_forced_update")
    private Boolean forcedUpdate;

    @ApiModelProperty(value = "是否已发布，0-未发布，1->已发布")
    @TableField("is_released")
    private Boolean released;

    @ApiModelProperty(value = "修改人ID")
    private Long pcUserId;

}
