package com.ys.mail.model.map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 地图数据
 *
 * @author CRH
 * @date 2022-04-23 13:58
 * @since 1.0
 */
@Data
@Builder
@ApiModel(value = "MapDataDTO", description = "地图数据")
public class MapDataDTO implements Serializable {

    private static final long serialVersionUID = -3139319269253518808L;

    @ApiModelProperty(value = "纬度")
    private Double lat;

    @ApiModelProperty(value = "经度")
    private Double lng;
}
