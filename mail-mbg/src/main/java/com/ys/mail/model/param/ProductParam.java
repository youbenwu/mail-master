package com.ys.mail.model.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * @author ghdhj
 */
@Data
public class ProductParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "最后一个id,首次可以传null,就是查询最新的20条")
    @Min(value = 1000000000000000000L,message = "请输入正确的传参")
    private Long productId;

    @ApiModelProperty(value = "标记,默认传null和false就是首页的数据,true就是会员这边的数据")
    private Boolean flag;

    @ApiModelProperty(value = "1精选,2精致,3穿搭,4生活,默认为null就是查精致")
    @Range(min = 1,max = 4,message = "请输入合适的参数{1,4}")
    private Byte hType;
}
