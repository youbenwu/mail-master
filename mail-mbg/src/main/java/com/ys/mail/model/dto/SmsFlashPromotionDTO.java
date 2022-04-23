package com.ys.mail.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 24
 * @date 2021/12/30 9:45
 * @description sms_flash_promotion DTO
 */
@Data
public class SmsFlashPromotionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long flashPromotionId;
    @ApiModelProperty(value = "描述")
    private String flashPromotionTitle;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date startTime;
    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date endTime;
    @ApiModelProperty(value = "上下线状态,0->下线,1->上线")
    @TableField("is_publish_status")
    private Boolean publishStatus;
    @ApiModelProperty(value = "是否展示于首页:0->false,1->true")
    @TableField("is_home_status")
    private Boolean homeStatus;
    @ApiModelProperty(value = "0->大尾狐,1->呼啦兔场次")
    private Byte cpyType;
}
