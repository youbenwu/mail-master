package com.ys.mail.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 限时购表
 * </p>
 *
 * @author 070
 * @since 2021-11-12
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SmsFlashPromotion对象", description="限时购表")
public class SmsFlashPromotion implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "flash_promotion_id", type = IdType.INPUT)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long flashPromotionId;

    @ApiModelProperty(value = "描述")
    private String flashPromotionTitle;

    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "上下线状态,0->下线,1->上线")
    @TableField("is_publish_status")
    private Boolean publishStatus;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "是否展示于首页:0->false,1->true")
    @TableField("is_home_status")
    private Boolean homeStatus;

    @ApiModelProperty(value = "0->大尾狐,1->呼啦兔场次")
    private Byte cpyType;

    /**
     * 构造器,修改是否展示于首页构造器
     */
    public SmsFlashPromotion(Long flashPromotionId,Boolean homeStatus){
        this.flashPromotionId = flashPromotionId;
        this.homeStatus = homeStatus;
    }

    /**
     * 构造器
     * @param publishStatus 上下线状态
     * @param flashPromotionId id
     */
    public SmsFlashPromotion(Boolean publishStatus,Long flashPromotionId){
        this.publishStatus = publishStatus;
        this.flashPromotionId = flashPromotionId;
    }

}
