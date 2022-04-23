package com.ys.mail.model.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author DT
 * @version 1.0
 * @date 2021-12-30 15:09
 */
@Data
public class QuickOrderQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "定义翻页的条数,默认是20条",required = true)
    @Min(value = 1)
    @NotNull
    private Integer pageSize = 20;

    @ApiModelProperty(value = "定义的翻页,首次传0,每次传最后一个",required = true)
    @NotNull
    private Long orderId = 0L;

    @ApiModelProperty(value = "查询条件,商品名称,可为空")
    private String keyword;

    @ApiModelProperty(value = "类型查询:0->待支付,1->待发货,2->待收货,3->已完成,7->待评价,传-1查全部,8->已核销,6->已付款")
    @Range(min = -1,max = 8)
    @NotNull
    private Byte type;

    @ApiModelProperty(value = "公司类型订单:0->大尾狐,1->呼啦兔",required = true)
    @NotNull
    @Range(min = 0,max = 1)
    private Byte cpyType;
}
