package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.PmsPartnerProduct;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.param.PcPartnerProductParam;

/**
 * <p>
 * 合伙人产品表 服务类
 * </p>
 *
 * @author 070
 * @since 2022-02-24
 */
public interface PmsPartnerProductService extends IService<PmsPartnerProduct> {
    /**
     * 合伙人产品新增和修改
     * @param param 实体
     * @return 返回值
     */
    CommonResult<Boolean> create(PcPartnerProductParam param);
}
