package com.ys.mail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.mail.entity.PmsVerificationCode;
import com.ys.mail.model.dto.VerifyDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 070
 * @since 2022-01-24
 */
@Mapper
public interface PmsVerificationCodeMapper extends BaseMapper<PmsVerificationCode> {

    VerifyDto queryDetail(String code);
}
