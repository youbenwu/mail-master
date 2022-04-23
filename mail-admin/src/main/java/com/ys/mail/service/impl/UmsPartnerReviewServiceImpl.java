package com.ys.mail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.entity.UmsPartner;
import com.ys.mail.entity.UmsPartnerReview;
import com.ys.mail.mapper.UmsPartnerMapper;
import com.ys.mail.mapper.UmsPartnerReviewMapper;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.query.QueryParentQuery;
import com.ys.mail.model.vo.UmsPartnerReviewVO;
import com.ys.mail.service.UmsPartnerReviewService;
import com.ys.mail.service.UmsPartnerService;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.IdWorker;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 070
 * @since 2022-01-19
 */
@Service
public class UmsPartnerReviewServiceImpl extends ServiceImpl<UmsPartnerReviewMapper, UmsPartnerReview> implements UmsPartnerReviewService {

    @Autowired
    private UmsPartnerReviewMapper partnerReviewMapper;
    @Autowired
    private UmsPartnerService partnerService;
    @Autowired
    private UmsPartnerMapper partnerMapper;

    @Override
    public CommonResult<Page<UmsPartnerReview>> list(QueryParentQuery query) {

        QueryWrapper<UmsPartnerReview> wrapper = new QueryWrapper<>();
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
        if (StringUtils.isNotBlank(query.getAuthStatus())) {
            wrapper.eq("auth_status", query.getAuthStatus());
        }

        Page<UmsPartnerReview> page = new Page<>(query.getPageNum(), query.getPageSize());
        Page<UmsPartnerReview> partnerReviewPage = this.page(page, wrapper);
        return CommonResult.success(partnerReviewPage);
    }

    @Override
    public UmsPartnerReviewVO getInfoById(Long partnerReviewId) {
        UmsPartnerReview umsPartnerReview = partnerReviewMapper.selectById(partnerReviewId);
        if (ObjectUtils.isEmpty(umsPartnerReview)) {
            return null;
        }
        return new UmsPartnerReviewVO(umsPartnerReview);
    }

    @Override
    @Transactional
    public CommonResult<Boolean> succeed(Long partnerReviewId) {
        UmsPartnerReview umsPartnerReview = partnerReviewMapper.selectById(partnerReviewId);
        if (ObjectUtils.isEmpty(umsPartnerReview)) {
            return CommonResult.failed("合伙人资料不存在");
        }
        umsPartnerReview.setAuthStatus(UmsPartnerReview.AUTH_STATUS_ONE);
        boolean b = saveOrUpdate(umsPartnerReview);
        if (b) {
            // 查询userid 是否已经存在
            Long userId = umsPartnerReview.getUserId();
            List<UmsPartner> umsPartners = partnerMapper.selectList(Wrappers.<UmsPartner>lambdaQuery().eq(UmsPartner::getUserId, userId));
            if (BlankUtil.isEmpty(umsPartners)) {
                // 新增合伙人数据
                UmsPartner umsPartner = new UmsPartner();
                BeanUtils.copyProperties(umsPartnerReview, umsPartner);
                umsPartner.setPartnerId(IdWorker.generateId());
                b = partnerService.save(umsPartner);
            }
        }
        return b ? CommonResult.success("success", true) : CommonResult.failed("error", false);
    }

    @Override
    @Transactional
    public CommonResult<Boolean> fail(Long partnerReviewId) {
        UmsPartnerReview umsPartnerReview = partnerReviewMapper.selectById(partnerReviewId);
        if (umsPartnerReview.getAuthStatus().equals(UmsPartnerReview.AUTH_STATUS_ONE)) {
            return CommonResult.success(true);
        }
        umsPartnerReview.setAuthStatus(UmsPartnerReview.AUTH_STATUS_ZEO);
        boolean b = saveOrUpdate(umsPartnerReview);
        return b ? CommonResult.success("success", true) : CommonResult.failed("error", false);
    }
}
