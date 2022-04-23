package com.ys.mail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ys.mail.entity.UmsPartnerReview;
import com.ys.mail.model.vo.UmsPartnerReviewVO;
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
public interface UmsPartnerReviewMapper extends BaseMapper<UmsPartnerReview> {

    Page<UmsPartnerReviewVO> list(@Param("page") Page<UmsPartnerReviewVO> page);
}
