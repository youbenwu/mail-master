package com.ys.mail.mapper;

import com.ys.mail.entity.PmsVerificationRecords;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.mail.model.dto.OrderDetailDto;
import com.ys.mail.model.vo.PartnerTodayResultsVO;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 核销记录表 Mapper 接口
 * </p>
 *
 * @author 070
 * @since 2022-01-24
 */
@Mapper
public interface PmsVerificationRecordsMapper extends BaseMapper<PmsVerificationRecords> {

    OrderDetailDto selectOrderDetail(Long recordId);

    PartnerTodayResultsVO partnerTodayResults(Long partnerId);
}
