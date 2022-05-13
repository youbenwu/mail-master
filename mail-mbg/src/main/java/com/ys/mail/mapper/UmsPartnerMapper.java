package com.ys.mail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.mail.entity.UmsPartner;
import com.ys.mail.model.dto.PartnerAddressDTO;
import com.ys.mail.model.dto.PartnerUserDTO;
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

    /**
     * 根据供应商ID获取供应商信息和用户信息
     *
     * @param partnerId 供应商ID
     * @return 结果
     */
    PartnerUserDTO getPartnerInfoById(@Param("partnerId") Long partnerId);

    /**
     * 根据合伙人用户ID获取合伙人信息
     *
     * @param userId 合伙人用户ID
     * @return 合伙人信息
     */
    UmsPartner getPartnerByUserId(@Param("userId") Long userId);
}
