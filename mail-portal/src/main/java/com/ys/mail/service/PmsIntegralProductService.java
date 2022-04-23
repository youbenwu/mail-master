package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.PmsIntegralProduct;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.param.IntegralConvertParam;

import java.util.List;

/**
 * <p>
 * 积分商品信息表 服务类
 * </p>
 *
 * @author 070
 * @since 2021-11-17
 */
public interface PmsIntegralProductService extends IService<PmsIntegralProduct> {
    /**
     * 积分商城的兑换商品
     * @param integralPdtId 积分换购商品id
     * @return 返回值
     */
    List<PmsIntegralProduct> getAllIntegralPdt(Long integralPdtId);

    /**
     * 用户兑换商品
     * @param param 实体对象
     * @return 返回值
     */
    CommonResult<Boolean> integralConvertPdt(IntegralConvertParam param);
}
