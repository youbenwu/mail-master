package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.PmsVerificationRecords;
import com.ys.mail.entity.UmsPartner;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.query.DetailQuery;
import com.ys.mail.model.admin.query.Query;
import com.ys.mail.model.dto.OrderDetailDto;
import com.ys.mail.model.dto.OrderInfoDTO;
import com.ys.mail.model.dto.PartnerAddressDTO;
import com.ys.mail.model.vo.ElectronicVo;
import com.ys.mail.model.vo.MerchandiseVo;
import com.ys.mail.model.vo.PartnerTodayResultsVO;

import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 070
 * @since 2022-01-19
 */
public interface UmsPartnerService extends IService<UmsPartner> {

    CommonResult<PartnerTodayResultsVO> todayResults();

    CommonResult<MerchandiseVo> merchandise(Query query);

    CommonResult<ElectronicVo> electronic(Query query);

    CommonResult<Boolean> verification(Map<String, String> params);

    CommonResult<PmsVerificationRecords> queryDetail(DetailQuery query);

    CommonResult<OrderDetailDto> verifDetail(Long recordId);

    CommonResult<PmsVerificationRecords> verificationList(Query query);

    CommonResult<OrderInfoDTO> orderDetail(Long orderId);

    PartnerAddressDTO getAddressByProductId(Long productId);

    /**
     * 根据合伙人用户ID获取合伙人信息
     *
     * @param userId 合伙人用户ID
     * @return 合伙人信息
     */
    UmsPartner getPartnerByUserId(Long userId);
}
