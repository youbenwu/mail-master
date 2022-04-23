package com.ys.mail.model.po;

import com.ys.mail.entity.PmsSkuStock;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author ghdhj
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MebSkuPO extends PmsSkuStock implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal disCount;
}
