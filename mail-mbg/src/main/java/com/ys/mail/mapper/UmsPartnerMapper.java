package com.ys.mail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ys.mail.entity.UmsPartner;
import com.ys.mail.model.vo.UmsPartnerVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 070
 * @since 2022-01-19
 */
@Mapper
public interface UmsPartnerMapper extends BaseMapper<UmsPartner> {

    Page<UmsPartnerVo> list(@Param("page") Page<UmsPartnerVo> page);
}
