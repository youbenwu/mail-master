package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.UmsPartnerReview;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.dto.UmsPartnerReviewDTO;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 070
 * @since 2022-01-19
 */
public interface UmsPartnerReviewService extends IService<UmsPartnerReview> {

    CommonResult<String> apply(UmsPartnerReviewDTO req);

    CommonResult<Map<String, Object>> judge();
}
