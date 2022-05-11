package com.ys.mail.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ys.mail.enums.IPairs;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author 070
 * @since 2022-01-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "PmsVerificationCode对象", description = "")
public class PmsVerificationCode implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "核销码id")
    @TableId(value = "verification_id", type = IdType.INPUT)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long verificationId;

    @ApiModelProperty(value = "合伙人id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long partnerId;

    @ApiModelProperty(value = "核销编码")
    private String code;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "状态：0待使用 1已使用 2已失效")
    private Integer isStatus;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "逻辑删除：0->未删除,1->删除")
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleted;

    @ApiModelProperty(value = "截止时间")
    private Date expireTime;

    @AllArgsConstructor
    public enum IsStatus implements IPairs<Integer, String, IsStatus> {
        /**
         * 状态
         */
        ZERO(0, "待使用"),
        ONE(1, "已使用"),
        TWO(2, "已失效"),
        ;
        final Integer type;
        final String name;

        @Override
        public Integer key() {
            return this.type;
        }

        @Override
        public String value() {
            return this.name;
        }
    }


}
