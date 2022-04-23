package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.UmsIdyAuth;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.param.IdyAuthParam;

/**
 * <p>
 * 用户身份认证 服务类
 * </p>
 *
 * @author 070
 * @since 2021-11-22
 */
public interface UmsIdyAuthService extends IService<UmsIdyAuth> {
    /**
     * 身份证认证审核
     * @param param 参数对象
     * @return 返回值
     */
    CommonResult<Boolean> createIdy(IdyAuthParam param);
}
