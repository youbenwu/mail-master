package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.UmsUser;
import com.ys.mail.entity.UmsUserBlacklist;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.param.UmsUserBlackListParam;
import com.ys.mail.model.admin.query.UserBlackListQuery;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 070
 * @since 2022-01-30
 */
public interface UmsUserBlacklistService extends IService<UmsUserBlacklist> {

    /**
     * 是否存在黑名单中
     *
     * @param phone 用户登录的手机号码
     * @return true -> 存在黑名单中; false -> 不存在黑名单中，允许注册登录
     */
    Boolean isOnBlackList(String phone);

    /**
     * 多条件分页列表
     *
     * @param query 查询条件
     * @return 列表
     */
    CommonResult<Page<UmsUserBlacklist>> getPage(UserBlackListQuery query);

    /**
     * 单条查询
     *
     * @param blId 黑名单ID
     * @return 单条记录
     */
    CommonResult<UmsUserBlacklist> getOne(Long blId);

    /**
     * 添加修改
     *
     * @param param    参数
     * @param pcUserId 操作用户ID
     * @return 结果
     */
    CommonResult<Boolean> update(UmsUserBlackListParam param, Long pcUserId);

    /**
     * 删除单条
     *
     * @param blId 黑名单ID
     * @return 删除结果
     */
    CommonResult<Boolean> deleteOne(Long blId);

    /**
     * 注销账号
     *
     * @param user 用户对象
     * @return 返回值
     */
    boolean cancelAccount(UmsUser user);

    /**
     * 检查手机号是否存在黑名单中，并抛出异常
     *
     * @param phone 手机号
     */
    void checkPhone(String phone);
}
