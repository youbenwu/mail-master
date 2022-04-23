package com.ys.mail.model.param;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author DT
 * @version 1.0
 * @date 2021-11-18 15:30
 */
@Data
public class ProductCommentParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品id", required = true)
    @NotBlank
    @Pattern(regexp = "^\\d{19}$")
    @NotBlank
    private String productId;

    @ApiModelProperty(value = "商品评价星数：0->5")
    @Range(min = 0, max = 5)
    private Integer productStar;

    @ApiModelProperty(value = "快递包装星数：0->5")
    @Range(min = 0, max = 5)
    private Integer courierStar;

    @ApiModelProperty(value = "送货速度星数：0->5")
    @Range(min = 0, max = 5)
    private Integer deliveryStar;

    @ApiModelProperty(value = "配送员星数：0->5")
    @Range(min = 0, max = 5)
    private Integer markStar;

    @ApiModelProperty(value = "是否匿名,0->false,1->true")
    @TableField("is_anonymous")
    @NotBlank
    private Boolean anonymous;

    @ApiModelProperty(value = "商品评论内容")
    @NotBlank
    private String productContent;

    @ApiModelProperty(value = "上传图片地址;以逗号隔开,最多5张")
    private String pics;

    @ApiModelProperty(value = "订单商品ID")
    @NotBlank
    private Long OrderItemId;


}
