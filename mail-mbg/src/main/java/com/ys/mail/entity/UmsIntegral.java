package com.ys.mail.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 积分变化历史记录表
 * </p>
 *
 * @author 070
 * @since 2021-11-17
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="UmsIntegral对象", description="积分变化历史记录表")
public class UmsIntegral implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "integral_id", type = IdType.INPUT)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long integralId;

    @ApiModelProperty(value = "用户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    @ApiModelProperty(value = "操作人员,后台管理人员,存储的id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long pcUserId;

    @ApiModelProperty(value = "操作备注")
    private String operateNote;

    @ApiModelProperty(value = "改变类型：0->增加；1->减少")
    private Integer changeType;

    @ApiModelProperty(value = "积分改变数量")
    private Long changeCount;

    @ApiModelProperty(value = "积分来源：0->购物;1->管理员修改;2->邀请增加")
    private Integer sourceType;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date updateTime;


}
