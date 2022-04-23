package com.ys.mail.model.vo;

import lombok.Data;

/**
 * @author 24
 * @date 2022/1/19 10:24
 * @description 合伙人今日业绩
 */
@Data
public class PartnerTodayResultsVO {
    /* 订单数 */
    private Integer orderNumber;
    /* 金额 */
    private Long totalAmount;
    /* 访客数 */
    private Integer visitors;
    /* 均价 */
    private Long avgPrice;

    public PartnerTodayResultsVO() {
    }

    public PartnerTodayResultsVO(Integer value) {
        this.orderNumber = 0;
        this.totalAmount = 0L;
        this.visitors = 0;
        this.avgPrice = 0L;
    }
}
