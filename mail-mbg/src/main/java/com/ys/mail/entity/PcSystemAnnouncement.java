package com.ys.mail.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * pc端公告表
 * </p>
 *
 * @author 070
 * @since 2021-12-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="PcSystemAnnouncement对象", description="pc端公告表")
public class PcSystemAnnouncement implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "公告id")
    @TableId(value = "system_announcement_id", type = IdType.INPUT)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long systemAnnouncementId;

    @ApiModelProperty(value = "公告标题")
    private String systemAnnouncementTitle;

    @ApiModelProperty(value = "公告内容")
    private String systemAnnouncementContent;

    @ApiModelProperty(value = "发布状态")
    private Boolean systemAnnouncementState;

    @ApiModelProperty(value = "公告时间")
    private Date systemAnnouncementTime;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "创建人")
    private Long userId;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "逻辑删除：0->未删除；1->删除")
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleted;


}
