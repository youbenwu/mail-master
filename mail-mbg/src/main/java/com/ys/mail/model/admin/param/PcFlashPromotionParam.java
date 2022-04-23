package com.ys.mail.model.admin.param;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author DT
 * @version 1.0
 * @date 2021-11-12 11:44
 */
@Data
public class PcFlashPromotionParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id",required = true)
    @NotNull
    private Long flashPromotionId;

    @ApiModelProperty(value = "描述",required = true)
    @NotBlank
    private String flashPromotionTitle;

    @ApiModelProperty(value = "开始时间",required = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    //@Future
    @NotNull
    private Date startTime;

    @ApiModelProperty(value = "结束时间",required = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    //@Future
    @NotNull
    private Date endTime;

    @ApiModelProperty(value = "上下线状态,0->下线,1->上线",required = true)
    @TableField("is_publish_status")
    @NotNull
    private Boolean publishStatus;

    @ApiModelProperty(value = "0->大尾狐,1->呼啦兔场次")
    @NotNull
    private Byte cpyType;

    @ApiModelProperty(value = "是否展示于首页:0->false,1->true")
    private Boolean homeStatus;

}
