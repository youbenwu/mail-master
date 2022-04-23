package com.ys.mail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.entity.UmsPartner;
import com.ys.mail.mapper.UmsPartnerMapper;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.query.QueryParentQuery;
import com.ys.mail.model.vo.UmsPartnerVo;
import com.ys.mail.service.UmsPartnerService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 070
 * @since 2022-01-19
 */
@Service
public class UmsPartnerServiceImpl extends ServiceImpl<UmsPartnerMapper, UmsPartner> implements UmsPartnerService {

    @Autowired
    private UmsPartnerMapper partnerMapper;

    @Override
    public CommonResult<Page<UmsPartner>> list(QueryParentQuery query) {

        QueryWrapper<UmsPartner> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(query.getRealName())) {
            wrapper.like("real_name", query.getRealName());
        }
        if (StringUtils.isNotBlank(query.getCorporateName())) {
            wrapper.like("corporate_name", query.getCorporateName());
        }
        if (StringUtils.isNotBlank(query.getPhone())) {
            wrapper.like("phone", query.getPhone());
        }
        if (StringUtils.isNotBlank(query.getIdyCard())) {
            wrapper.like("idy_card", query.getIdyCard());
        }
        if (StringUtils.isNotBlank(query.getCreateTime()) && StringUtils.isNotBlank(query.getEndTime())) {
            wrapper.between("create_time", query.getCreateTime(), query.getEndTime());
        }

        Page<UmsPartner> page = new Page<>(query.getPageNum(), query.getPageSize());
        Page<UmsPartner> partnerReviewPage = this.page(page, wrapper);
        return CommonResult.success(partnerReviewPage);
    }

    @Override
    public CommonResult<UmsPartnerVo> getInfoById(Long partnerId) {
        UmsPartner umsPartner = getById(partnerId);
        if (ObjectUtils.isEmpty(umsPartner)) {
            return CommonResult.failed("没有合伙人信息");
        }
        UmsPartnerVo vo = new UmsPartnerVo();
        BeanUtils.copyProperties(umsPartner, vo);
        return CommonResult.success(vo);
    }

    @Override
    public CommonResult<Boolean> delete(Long partnerId) {
        return removeById(partnerId) ? CommonResult.success("success", true) : CommonResult.failed("error", false);
    }
}
