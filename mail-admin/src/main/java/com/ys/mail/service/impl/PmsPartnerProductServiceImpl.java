package com.ys.mail.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.entity.PmsPartnerProduct;
import com.ys.mail.mapper.PmsPartnerProductMapper;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.param.PcPartnerProductParam;
import com.ys.mail.service.PmsPartnerProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 合伙人产品表 服务实现类
 * </p>
 *
 * @author 070
 * @since 2022-02-24
 */
@Service
public class PmsPartnerProductServiceImpl extends ServiceImpl<PmsPartnerProductMapper, PmsPartnerProduct> implements PmsPartnerProductService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommonResult<Boolean> create(PcPartnerProductParam param) {
        return saveOrUpdate(param.getParam(param)) ? CommonResult.success(Boolean.TRUE) : CommonResult.failed(Boolean.FALSE);
    }
}
