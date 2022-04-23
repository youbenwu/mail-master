package com.ys.mail.model.dto;

import lombok.Data;

/**
 * @author 24
 * @date 2022/1/24 11:04
 * @description
 */
@Data
public class VerifyDto {

    private String orderSn;

    private String userId;

    private String productName;

    private String productPic;

    private Long productPrice;

    private Integer payType;

}
