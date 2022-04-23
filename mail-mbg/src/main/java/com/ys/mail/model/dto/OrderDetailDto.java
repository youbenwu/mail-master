package com.ys.mail.model.dto;

import lombok.Data;

/**
 * @author 24
 * @date 2022/1/24 15:33
 * @description
 */
@Data
public class OrderDetailDto {
    private Long recordId;
    private String code;
    private String productName;
    private String partnerName;
    private String productPic;
    private Long productQuantity;
    private Long totalAmount;
    private Long userId;
    private String receiverProvince;
    private String receiverName;
    private String orderId;
    private String createTime;
    private String verTime;
    private String orderSn;
    private String orderNote;
    private Integer status;
}
