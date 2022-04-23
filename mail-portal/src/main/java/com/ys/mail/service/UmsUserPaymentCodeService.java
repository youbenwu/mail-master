package com.ys.mail.service;

import com.ys.mail.entity.UmsUserPaymentCode;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.param.UserTemPayCodeParam;

/**
 * <p>
 * 用户支付密码表 服务类
 * </p>
 *
 * @author 070
 * @since 2021-12-08
 */
public interface UmsUserPaymentCodeService extends IService<UmsUserPaymentCode> {


    /**
     * 删除支付密码
     * @param userId
     * @return
     */
   /* Boolean removePaymentCode(Long userId);*/

    /**
     * 设置支付密码
     * @param param 实体参数
     * @return 返回值
     */
   /* CommonResult<Boolean> setPaymentCode(UserTemPayCodeParam param);*/

    /**
     * 验证支付密码
     * @param paymentCode md5加密后再通过aes加密的支付密码
     * @return 返回值
     */
    /*Boolean verifyPaymentCode(String paymentCode);*/

    /**
     * 修改支付密码
     * @param oldPaymentCode 旧的支付密码
     * @param password 原始密码
     * @return 返回值
     */
    /*CommonResult<Boolean> updatePaymentCode(String oldPaymentCode, String password);*/
}
