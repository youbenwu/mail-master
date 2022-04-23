package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.UmsIntegral;
import com.ys.mail.entity.UmsUser;

import java.util.List;

/**
 * <p>
 * 积分变化历史记录表 服务类
 * </p>
 *
 * @author 070
 * @since 2021-11-17
 */
public interface UmsIntegralService extends IService<UmsIntegral> {
    /**
     * 用户积分详情
     * @return 返回值
     */
    UmsUser getIntegralInfo();

    /**
     * 积分明细
     * @param integralId 积分明细id
     * @return 返回值
     */
    List<UmsIntegral> getAllIntegral(Long integralId);
}
