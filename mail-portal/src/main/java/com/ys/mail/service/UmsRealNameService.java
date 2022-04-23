package com.ys.mail.service;

import com.ys.mail.entity.UmsRealName;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.model.CommonResult;

/**
 * <p>
 * 实名认证表 服务类
 * </p>
 *
 * @author 070
 * @since 2021-12-17
 */
public interface UmsRealNameService extends IService<UmsRealName> {

    /**
     * <p>
     * 实名认证表 服务类
     * </p>
     *
     * @author 070
     * @since 2021-12-17
     */
    CommonResult<Boolean> updateGifeState();
}
