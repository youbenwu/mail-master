package com.ys.mail.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 我的店铺信息
 *
 * @author ghdhj
 */
@Data
public class MyStoreDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品秒杀价格")
    private Long flashPromotionPrice;

    @ApiModelProperty(value = "公司类型订单:0->大尾狐,1->呼啦兔")
    private Byte cpyType;

    @ApiModelProperty(value = "上架状态：0->下架；1->上架")
    @TableField("is_publish_status")
    private Boolean publishStatus;

    @ApiModelProperty(value = "秒杀商品状态：-1->已过期；1->已卖出；2->秒杀中；3->上架; 4->待卖出")
    private Integer flashProductStatus;

    @ApiModelProperty(value = "翻页id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long flashPromotionPdtId;

    @ApiModelProperty(value = "商品id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long productId;

    @ApiModelProperty(value = "用户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    @ApiModelProperty(value = "订单id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long orderId;

    @ApiModelProperty(value = "商品对象")
    private StoreProduct storeProduct;

    @ApiModelProperty(value = "截止时间，作用于合伙人商品，过期则不能上架、核销等")
    private Date expireTime;

    @Data
    static class StoreProduct {

        @ApiModelProperty(value = "商品图片")
        private String pic;

        @ApiModelProperty(value = "商品名称")
        private String productName;
    }

}
