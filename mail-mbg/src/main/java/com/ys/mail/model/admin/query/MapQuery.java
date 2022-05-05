package com.ys.mail.model.admin.query;

import com.ys.mail.annotation.BlankOrPattern;
import com.ys.mail.enums.RegularEnum;
import com.ys.mail.util.BlankUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.io.Serializable;

/**
 * 位置查询对象
 *
 * @author CRH
 * @date 2022-04-27 18:09
 * @since 1.0
 */
@Getter
@ApiModel(value = "MapQuery", description = "位置查询对象，默认北京天安门")
public class MapQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "纬度，范围：-90~90，小数长度最大10位")
    @BlankOrPattern(regEnum = RegularEnum.LAT, message = "纬度范围：-90~90")
    private Double lat = 39.909652;

    @ApiModelProperty(value = "经度，范围：-180~180，小数长度最大10位")
    @BlankOrPattern(regEnum = RegularEnum.LNG, message = "经度范围：-180~180")
    private Double lng = 116.404177;

    public void setLat(Double lat) {
        if (BlankUtil.isNotEmpty(lat)) {
            this.lat = lat;
        }
    }

    public void setLng(Double lng) {
        if (BlankUtil.isNotEmpty(lng)) {
            this.lng = lng;
        }
    }
}
