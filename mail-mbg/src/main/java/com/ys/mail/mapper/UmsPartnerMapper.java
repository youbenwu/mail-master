package com.ys.mail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.mail.entity.UmsPartner;
import com.ys.mail.model.dto.PartnerAddressDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 070
 * @since 2022-01-19
 */
@Mapper
public interface UmsPartnerMapper extends BaseMapper<UmsPartner> {

    /**
     * 根据商品ID获取合伙人地址信息
     *
     * @param productId 合伙人商品ID
     * @return 地址信息
     */
    PartnerAddressDTO getAddressByProductId(@Param("productId") Long productId);
}
