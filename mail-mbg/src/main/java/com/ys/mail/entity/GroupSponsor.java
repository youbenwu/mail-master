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
 * 拼团发起表
 * </p>
 *
 * @author 070
 * @since 2021-12-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="GroupSponsor对象", description="拼团发起表")
public class GroupSponsor implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "group_sponsor_id", type = IdType.INPUT)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long groupSponsorId;

    @ApiModelProperty(value = "用户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    @ApiModelProperty(value = "关联的团购id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long groupBuyId;

    @ApiModelProperty(value = "参团人数,代表有几人参加了,发起人那就是只有一个")
    private Integer joinNum;

    @ApiModelProperty(value = "状态:1->成功,2->正在进行中,3代表失败")
    private Byte groupStatus;

    @ApiModelProperty(value = "拼团规模,后面需求改动可以加上在,暂时不用")
    private Integer scale;

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
    private Byte deleted;

}
