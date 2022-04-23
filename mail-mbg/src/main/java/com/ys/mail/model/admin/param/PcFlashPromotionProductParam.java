package com.ys.mail.model.admin.param;

import com.ys.mail.entity.SmsFlashPromotionProduct;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.IdWorker;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author DT
 * @version 1.0
 * @date 2021-11-12 14:26
 */
@Data
public class PcFlashPromotionProductParam implements Serializable {

    @ApiModelProperty(value = "主键id,首次传0",required = true)
    @Pattern(regexp = "^\\d{19}$")
    private String flashPromotionPdtId;

    @ApiModelProperty(value = "限时购id",required = true)
    @NotBlank
    @Pattern(regexp = "^\\d{19}$")
    private String flashPromotionId;

    @ApiModelProperty(value = "商品id",required = true)
    @NotBlank
    @Pattern(regexp = "^\\d{19}$")
    private String productId;

    @ApiModelProperty(value = "限时购价格,1分等于1,100等于1元",required = true)
    @NotNull
    private Long flashPromotionPrice;

    @ApiModelProperty(value = "限时购数量",required = true)
    @NotNull
    private Integer flashPromotionCount;

    @ApiModelProperty(value = "每人限购数量",required = true)
    @NotNull
    private Integer flashPromotionLimit;

    @ApiModelProperty(value = "排序")
    @NotNull
    private Integer sort;

    @ApiModelProperty("上架状态:0->下架;1->上架")
    @NotNull
    private Boolean isPublishStatus;

    @ApiModelProperty(value = "秒杀商品状态：1->已卖出；2->秒杀中；3->上架")
    @NotNull
    @Range(min = 1,max = 3)
    private Integer flashProductStatus;

    @ApiModelProperty(value = "原总数量")
    @NotNull
    @Min(value = 1)
    private Integer flashPromotionOriginCount;

    @ApiModelProperty(value = "公司类型订单:0->大尾狐,1->呼啦兔")
    @NotNull
    @Range(min = 0,max = 1)
    private Byte cpyType;

    @ApiModelProperty(value = "记录原价格")
    @NotNull
    private Long flashPromotionOriginPrice;

    @ApiModelProperty(value = "合伙人价格")
    /*@NotNull,添加合伙人商品时必须有值*/
    private Long partnerPrice;


    private PcFlashPromotionProductParam(){}

    /**
     * 构造param方法
     * @param param
     * @return
     */
    public SmsFlashPromotionProduct getParam(PcFlashPromotionProductParam param){
        SmsFlashPromotionProduct flashPromotionProduct = new SmsFlashPromotionProduct();
        BeanUtils.copyProperties(param,flashPromotionProduct);
        flashPromotionProduct.setFlashPromotionId(Long.valueOf(flashPromotionId));
        flashPromotionProduct.setFlashPromotionPdtId(BlankUtil.isEmpty(flashPromotionPdtId) ? IdWorker.generateId() : Long.valueOf(flashPromotionPdtId));
        flashPromotionProduct.setProductId(Long.valueOf(productId));
        return flashPromotionProduct;
    }

}
