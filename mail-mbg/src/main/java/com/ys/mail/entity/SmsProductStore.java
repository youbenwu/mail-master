package com.ys.mail.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ys.mail.enums.IPairs;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户_商品_店铺表
 * </p>
 *
 * @author 070
 * @since 2022-04-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "SmsProductStore对象", description = "用户_商品_店铺表")
public class SmsProductStore implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品店铺id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "pdt_store_id", type = IdType.INPUT)
    private Long pdtStoreId;

    @ApiModelProperty(value = "用户id，商品售卖人，店铺所属用户")
    private Long userId;

    @ApiModelProperty(value = "店铺Logo")
    private String storeLogo;

    @ApiModelProperty(value = "店铺名称，自定义")
    private String storeName;

    @ApiModelProperty(value = "店主名称，用户名称")
    private String storeBoss;

    @ApiModelProperty(value = "店主手机，用户手机")
    private String storePhone;

    @ApiModelProperty(value = "店主经营地址")
    private String storeAddress;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "逻辑删除：0->未删除；1->删除")
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleted;

    @ApiModelProperty(value = "审核人ID")
    private Long pcUserId;

    @ApiModelProperty(value = "审核状态：0->待审核，1->已通过，2->不通过")
    private Integer reviewState;

    @ApiModelProperty(value = "审核描述")
    private String reviewDesc;

    @Getter
    @AllArgsConstructor
    @Accessors(fluent = true)
    public enum ReviewState implements IPairs<Integer, String, ReviewState> {
        /**
         * 审核状态
         */
        ZERO(0, "待审核"),
        ONE(1, "已通过"),
        TWO(2, "不通过"),
        ;
        final Integer key;
        final String value;
    }

}
