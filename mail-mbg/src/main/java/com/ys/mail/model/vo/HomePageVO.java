package com.ys.mail.model.vo;

import com.ys.mail.entity.SmsHomeAdvertise;
import com.ys.mail.model.dto.SecondProductDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author DT
 * @version 1.0
 * @date 2021-11-25 16:16
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "首页信息")
public class HomePageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "首页轮播图")
    private List<SmsHomeAdvertise> homeAdvertises;

    @ApiModelProperty(value = "首页显示秒杀")
    private SecondProductDTO secondProductDTO;

    @ApiModelProperty(value = "COS的图片最新存储路径，前缀")
    private String cosFilePath;

}
