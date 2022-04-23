package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.UmsPartner;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.query.QueryParentQuery;
import com.ys.mail.model.vo.UmsPartnerVo;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 070
 * @since 2022-01-19
 */
public interface UmsPartnerService extends IService<UmsPartner> {

    CommonResult<Page<UmsPartner>> list(QueryParentQuery query);

    CommonResult<UmsPartnerVo> getInfoById(Long partnerId);

    CommonResult<Boolean> delete(Long partnerId);
}
