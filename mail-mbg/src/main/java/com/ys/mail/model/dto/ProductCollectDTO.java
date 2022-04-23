package com.ys.mail.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ys.mail.entity.PmsProduct;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 商品收藏列表实体对象
 * @author DT
 * @version 1.0
 * @date 2021-12-03 15:29
 */
@Data
public class ProductCollectDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品收藏id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long pdtCollectId;

    @ApiModelProperty(value = "用户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    @ApiModelProperty(value = "商品对象")
    private PmsProduct product;

    @ApiModelProperty(value = "商品收藏时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date createTime;
}
