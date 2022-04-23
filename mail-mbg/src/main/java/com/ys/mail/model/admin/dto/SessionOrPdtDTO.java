package com.ys.mail.model.admin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ys.mail.model.admin.vo.PromotionPdtVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author DT
 * @version 1.0
 * @date 2022-01-22 14:06
 */
@Data
public class SessionOrPdtDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long flashPromotionId;

    @ApiModelProperty(value = "描述")
    private String flashPromotionTitle;

    @ApiModelProperty(value = "0->大尾狐,1->呼啦兔场次")
    private Byte cpyType;

}
