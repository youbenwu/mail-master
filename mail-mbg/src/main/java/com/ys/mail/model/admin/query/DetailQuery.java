package com.ys.mail.model.admin.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 24
 * @date 2022/1/24 11:40
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DetailQuery extends Query{

    private Integer month;

    private Integer year;

}
