package com.ys.mail.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 参团列表
 * </p>
 *
 * @author 070
 * @since 2021-12-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="GroupTuxedo对象", description="参团列表")
public class GroupTuxedo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "group_tuxedo_id", type = IdType.INPUT)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long groupTuxedoId;

    @ApiModelProperty(value = "用户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    @ApiModelProperty(value = "发起id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long groupSponsorId;

    @ApiModelProperty(value = "状态(-1失败,1组团成功,2组团中)")
    private Byte groupStatus;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "是否团主:0->false,1->true")
    @TableField("is_host")
    private Boolean host;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "逻辑删除：0->未删除,1->删除")
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleted;


}
