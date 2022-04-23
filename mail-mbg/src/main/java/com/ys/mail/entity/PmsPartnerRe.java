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
 * 合伙人退还期数表
 * </p>
 *
 * @author 070
 * @since 2022-03-01
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "PmsPartnerRe对象", description = "合伙人退还期数表")
public class PmsPartnerRe implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态:0->未退还
     */
    public static final Byte PERIODS_STATUS_ONE = 1;

    /**
     * 状态:1->退还成功
     */
    public static final Byte PERIODS_STATUS_TWO = 2;

    /**
     * 状态:2->退还失败
     */
    public static final Byte PERIODS_STATUS_THREE = 3;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "partner_re_id", type = IdType.INPUT)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long partnerReId;

    @ApiModelProperty(value = "用户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    @ApiModelProperty(value = "订单id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long orderId;

    @ApiModelProperty(value = "期数,后台生成1,2,3,4等等")
    private Integer periodsNum;

    @ApiModelProperty(value = "退还每期金额")
    private Long periodsPrice;

    @ApiModelProperty(value = "描述")
    private String descSour;

    @ApiModelProperty(value = "状态:0->未退还,1->退还成功,2->退还失败")
    private Byte periodsStatus;

    @ApiModelProperty(value = "退还时间,yyyy-MM-dd,每月1号")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date periodsDate;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "返还数量,做统计有用的到")
    private Integer reNum;

    /**
     * 修改状态构造函数
     *
     * @param partnerReId   id
     * @param periodsStatus 状态
     */
    public PmsPartnerRe(Long partnerReId, Byte periodsStatus) {
        this.partnerReId = partnerReId;
        this.periodsStatus = periodsStatus;
    }
}
