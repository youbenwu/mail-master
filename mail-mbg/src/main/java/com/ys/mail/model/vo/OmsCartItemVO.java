package com.ys.mail.model.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ys.mail.entity.OmsCartItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 购物车表
 * </p>
 *
 * @author 070
 * @since 2021-11-16
 */
@Data
public class OmsCartItemVO extends OmsCartItem implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "商品名称")
    private String productName;
}
