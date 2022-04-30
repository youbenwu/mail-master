package com.ys.mail.model.vo;

import com.ys.mail.model.bo.FlashPromotionProductBO;
import com.ys.mail.model.dto.PartnerUserDTO;
import com.ys.mail.model.map.RedisGeoDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 附近店铺产品定位对象
 *
 * @author CRH
 * @date 2022-04-29 17:28
 * @since 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "NearbyStoreProductVO", description = "附近店铺产品定位")
public class NearbyStoreProductVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "附近店铺地理信息列表")
    private List<RedisGeoDTO> geoList;

    @ApiModelProperty(value = "当前默认店铺信息")
    private PartnerUserDTO partnerInfo;

    @ApiModelProperty(value = "当前默认店铺的商品列表")
    private List<FlashPromotionProductBO> productList;

}
