package com.ys.mail.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author liky
 * @date 2022/1/5 11:44
 * @description dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeActiveDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Date date;
    private Long userId;
    private String outTradeNo;
}
