package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.UmsPartnerReview;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.query.QueryParentQuery;
import com.ys.mail.model.vo.UmsPartnerReviewVO;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 070
 * @since 2022-01-19
 */
public interface UmsPartnerReviewService extends IService<UmsPartnerReview> {

    CommonResult<Page<UmsPartnerReview>> list(QueryParentQuery query);

    UmsPartnerReviewVO getInfoById(Long partnerReviewId);

    CommonResult<Boolean> succeed(Long partnerReviewId);

    CommonResult<Boolean> fail(Long partnerReviewId);
}
