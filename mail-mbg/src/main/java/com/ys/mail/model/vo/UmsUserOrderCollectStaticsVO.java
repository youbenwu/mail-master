package com.ys.mail.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 用户邀请信息表
 * </p>
 *
 * @author 070
 * @since 2021-11-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "UmsUserOrderCollectStatics对象", description = "用户订单收藏统计汇总")
public class UmsUserOrderCollectStaticsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户收藏数量")
    private Long productCollectCount;

    @ApiModelProperty(value = "商品足迹数量")
    private Long productLogCount;

    @ApiModelProperty(value = "用户优惠券数量")
    private Long smsCouponCount;

    @ApiModelProperty(value = "待付款订单的数量")
    private Long orderStatusNoPayCount;

    @ApiModelProperty(value = "待发货订单的数量")
    private Long orderStatusNoDeliverCount;

    @ApiModelProperty(value = "待收货订单的数量")
    private Long orderStatusNoChargedCount;

    @ApiModelProperty(value = "待评价订单的数量")
    private Long orderStatusNoAppraiseCount;

}
