package com.ys.mail.model.map;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Redis Geo 通用对象
 *
 * @author CRH
 * @date 2022-04-29 18:18
 * @since 1.0
 */
@Data
public class RedisGeoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id，标识")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty(value = "距离，单位m")
    private Double distance;

    @ApiModelProperty(value = "经度")
    private Double lng;

    @ApiModelProperty(value = "纬度")
    private Double lat;

}
