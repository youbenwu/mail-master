package com.ys.mail.model.admin.param;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ys.mail.entity.PmsPartnerProduct;
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
 * @author ghdhj
 */
@Data
public class PcPartnerProductParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "传null是新增,有值便是修改")
    @Pattern(regexp = "^\\d{19}$", message = " 长度必须为19位，请检查！")
    private String partnerPdtId;

    @ApiModelProperty(value = "商品id", required = true)
    @Pattern(regexp = "^\\d{19}$", message = " 长度必须为19位，请检查！")
    @NotBlank
    private String productId;

    @ApiModelProperty(value = "合伙人商品名称", required = true)
    @NotBlank
    private String partnerName;

    @ApiModelProperty(value = "价格,使用整数类型Long,1分就是1,100就是1元", required = true)
    @NotNull
    @Min(value = 1, message = "请输入正确的价格")
    private Long partnerPrice;

    @ApiModelProperty(value = "保证金,使用整数类型Long,1分就是1,100就是1元", required = true)
    @NotNull
    @Min(value = 1, message = "请输入正确的保证金")
    private Long earnestMoney;

    @ApiModelProperty(value = "总金额:价格+保证金,1分就是1,100就是1元", required = true)
    @NotNull
    @Min(value = 2, message = "请输入正确的总金额")
    private Long totalPrice;

    @ApiModelProperty(value = "服务描述")
    private String serveDesc;

    @ApiModelProperty(value = "退还期数(1-12期可以选择)", required = true)
    @NotNull
    @Range(min = 1, message = "最小期数为一期")
    private Integer rePeriods;

    @ApiModelProperty(value = "上下线状态,0->下线,1->上线")
    @TableField("is_publish_status")
    private Boolean publishStatus;

    @ApiModelProperty(value = "案例图片,最多5张")
    private String casePics;

    /**
     * 新增和修改构造函数
     *
     * @param param 实体
     * @return 返回实体
     */
    public PmsPartnerProduct getParam(PcPartnerProductParam param) {
        PmsPartnerProduct partnerProduct = new PmsPartnerProduct();
        BeanUtils.copyProperties(param, partnerProduct);
        partnerProduct.setPartnerPdtId(BlankUtil.isEmpty(partnerPdtId) ? IdWorker.generateId() : Long.valueOf(partnerPdtId));
        partnerProduct.setProductId(Long.valueOf(productId));
        return partnerProduct;
    }

}
