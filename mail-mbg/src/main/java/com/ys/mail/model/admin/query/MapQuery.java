package com.ys.mail.model.admin.query;

import com.ys.mail.annotation.BlankOrPattern;
import com.ys.mail.enums.RegularEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 经纬度查询条件对象
 *
 * @author CRH
 * @date 2022-04-27 18:09
 * @since 1.0
 */
@Data
@ApiModel(value = "MapQuery", description = "经纬度查询条件对象")
public class MapQuery implements Serializable {

    private static final long serialVersionUID = 1L;
    private static MapQuery mapQuery;

    static {
        // 默认中心地点的经纬度：北京天安门
        mapQuery = new MapQuery();
        mapQuery.setLat(39.909652);
        mapQuery.setLng(116.404177);
    }

    @ApiModelProperty(value = "纬度，范围：-90~90，小数长度最大10位")
    @BlankOrPattern(regEnum = RegularEnum.LAT, message = "纬度范围：-90~90")
    private Double lat;

    @ApiModelProperty(value = "经度，范围：-180~180，小数长度最大10位")
    @BlankOrPattern(regEnum = RegularEnum.LNG, message = "经度范围：-180~180")
    private Double lng;

    /**
     * 获取默认的中心点
     *
     * @return MapQuery
     */
    public static MapQuery getDefaultCentrePoint() {
        return mapQuery;
    }

}
