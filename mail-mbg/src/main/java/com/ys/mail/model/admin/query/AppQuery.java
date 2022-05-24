package com.ys.mail.model.admin.query;

import com.ys.mail.model.query.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 用一句简单的话来描述下该类
 *
 * @author CRH
 * @date 2022-05-16 18:20
 * @since 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AppQuery extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "APP名称")
    private String name;

    @ApiModelProperty(value = "APP文件上传状态：0->未上传，1->已上传")
    private Integer uploadStatus;

}
