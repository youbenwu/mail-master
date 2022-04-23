package com.ys.mail.service;

import com.ys.mail.entity.UmsUser;

/**
 * @author DT
 * @version 1.0
 * @date 2021-11-09 10:18
 */

public interface UserCacheService {
    /**
     * 根据用户手机号查询用户信息
     * @param phone 手机号
     * @return 返回对象
     */
    UmsUser getUser(String phone);

    /**
     * 设置用户缓存
     * @param user  用户对象
     */
    void setUser(UmsUser user);

    /**
     * 设置验证码的缓存时间
     * @param phone 用户手机号
     * @param authCode 验证码
     */
    void setAuthCode(String phone, String authCode);

    /**
     * 获取验证码
     * @param phone 手机号码
     * @return 返回值
     */
    String getAuthCode(String phone);

    /**
     * 根据用户名称删除缓存
     * @param phone 手机号
     */
    void delUser(String phone);
}
