package com.ys.mail.model.query;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author DT
 * @version 1.0
 * @date 2021-12-29 15:01
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CategorySearchQuery extends QueryObject implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 条件:查找商品，价格升和降,综合查全部,新品查是否是新品,keyword查,翻页条数
     */

    @ApiModelProperty(value = "是否新品,0->false,1->true")
    @TableField("is_new_status")
    private Boolean newStatus;

    @ApiModelProperty(value = "价格升序和倒序,0->升序,1->倒序,传null和传空就是查询所有")
    @Range(min = 0,max = 1,message = "价格输入查询有误")
    private Byte priceType;

    @ApiModelProperty(value = "销量升序和降序,0->升序,1->降序,传null和传空就是查询所有")
    @Range(min = 0,max = 1,message = "价格输入查询有误")
    private Byte saleType;

    @ApiModelProperty(value = "分类id")
    @NotBlank
    @Pattern(regexp = "^\\d{19}$")
    private String pdtCgyId;

}
