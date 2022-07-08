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

    // 多条件分页列表
    CommonResult<Page<UmsUserBlacklist>> getPage(UserBlackListQuery query);

    // 单条查询
    CommonResult<UmsUserBlacklist> getOne(Long blId);

    // 添加修改
    CommonResult<Boolean> update(UmsUserBlackListParam param, Long pcUserId);

    // 删除单条
    CommonResult<Boolean> deleteOne(Long blId);

    /**
     * 注销账号
     * @param user 用户对象
     * @return 返回值
     */
    boolean cancelAccount(UmsUser user);
}
