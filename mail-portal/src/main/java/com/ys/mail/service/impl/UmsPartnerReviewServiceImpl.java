package com.ys.mail.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.entity.UmsPartner;
import com.ys.mail.entity.UmsPartnerReview;
import com.ys.mail.mapper.UmsPartnerMapper;
import com.ys.mail.mapper.UmsPartnerReviewMapper;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.dto.UmsPartnerReviewDTO;
import com.ys.mail.model.map.MapDataDTO;
import com.ys.mail.service.UmsPartnerReviewService;
import com.ys.mail.util.BaiduMapUtil;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.IdWorker;
import com.ys.mail.util.UserUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private UmsPartnerMapper partnerMapper;

    /**
     * 合伙人申请
     *
     * @param req
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<String> apply(UmsPartnerReviewDTO req) {
        // 1. 查询是否已经审核通过或者有已经审核的记录

        List<UmsPartnerReview> umsPartnerReviews = partnerReviewMapper.selectList(Wrappers
                .<UmsPartnerReview>lambdaQuery()
                .eq(UmsPartnerReview::getUserId, UserUtil.getCurrentUser().getUserId())
                .ne(UmsPartnerReview::getAuthStatus, 0));
        if (BlankUtil.isNotEmpty(umsPartnerReviews)) {
            return CommonResult.failed("error", "申请失败-存在审核记录");
        }
        // 2. 新增审核记录
        UmsPartnerReview partnerReview = new UmsPartnerReview();
        BeanUtils.copyProperties(req, partnerReview);
        partnerReview.setPartnerReviewId(IdWorker.generateId());
        // 3. 获取地址的经纬度 ,TODO 需要做成异步更新
        String fullAddress = String.format("%s%s%s%s", req.getProvince(), req.getCity(), req.getRegion(), req.getAddress());
        MapDataDTO mapDataDTO = BaiduMapUtil.getLngAndLat(fullAddress);
        if (BlankUtil.isNotEmpty(mapDataDTO)) {
            partnerReview.setLatitude(mapDataDTO.getLat());
            partnerReview.setLongitude(mapDataDTO.getLng());
        }
        partnerReview.setUserId(UserUtil.getCurrentUser().getUserId());
        boolean b = save(partnerReview);

        return b ? CommonResult.success("success", "申请成功") : CommonResult.failed("error", "申请失败");
    }

    @Override
    public CommonResult<Map<String, Object>> judge() {

        Map<String, Object> map = new HashMap<>();
        Long userId = UserUtil.getCurrentUser().getUserId();

        UmsPartner umsPartner = partnerMapper.selectOne(Wrappers.<UmsPartner>lambdaQuery()
                                                                .eq(UmsPartner::getUserId, userId));
        if (ObjectUtils.isNotEmpty(umsPartner)) {
            map.put("isPartner", true);
        } else {
            map.put("isPartner", false);
            UmsPartnerReview umsPartnerReview = partnerReviewMapper.selectOne(Wrappers.<UmsPartnerReview>lambdaQuery()
                                                                                      .eq(UmsPartnerReview::getUserId, userId));
            if (ObjectUtils.isEmpty(umsPartnerReview)) {
                map.put("status", -1);
            } else {
                map.put("status", umsPartnerReview.getAuthStatus());
            }
        }
        return CommonResult.success(map);
    }
}
