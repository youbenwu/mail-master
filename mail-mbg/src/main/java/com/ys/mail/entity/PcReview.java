package com.ys.mail.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ys.mail.annotation.EnumDocumentValid;
import com.ys.mail.enums.IPairs;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author 070
 * @since 2021-12-29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "PcReview对象", description = "提现审核表")
public class PcReview implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "审核id")
    @TableId(value = "review_id", type = IdType.INPUT)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long reviewId;

    @ApiModelProperty(value = "审核状态")
    @EnumDocumentValid(enumClass = ReviewState.class, isValid = false)
    private Integer reviewState;

    @Getter
    @AllArgsConstructor
    @Accessors(fluent = true)
    public enum ReviewState implements IPairs<Integer, String, ReviewState> {
        /**
         * 系统调度触发
         */
        MINUS_ONE(-1, "已失效"),
        /**
         * 默认，只有当数据为0时，方可做其他操作
         */
        ZERO(0, "待审核"),
        /**
         * 后天审核操作
         */
        ONE(1, "已通过"),
        TWO(2, "不通过"),
        THREE(3, "已关闭"),
        /**
         * 该状态由用户主动取消
         */
        FOUR(4, "已取消"),
        ;
        final Integer key;
        final String value;
    }

    @ApiModelProperty(value = "审核人")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long pcUserId;

    @ApiModelProperty(value = "审核描述")
    private String reviewDescribe;

    @ApiModelProperty(value = "提现流水号")
    private String incomeId;

    @ApiModelProperty(value = "提现流水id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long exIncomeId;

    @ApiModelProperty(value = "手续费流水id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long rateIncomeId;

    @ApiModelProperty(value = "申请人")
    @NotBlank
    private Long userId;

    @ApiModelProperty(value = "申请提现金额")
    @NotBlank
    private Long reviewMoney;

    @ApiModelProperty(value = "支付宝账号")
    @NotBlank
    private String alipayAccount;

    @ApiModelProperty(value = "支付宝名字")
    @NotBlank
    private String alipayName;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "逻辑删除：0->未删除；1->删除")
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleted;

}
