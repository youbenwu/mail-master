package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.UmsUser;
import com.ys.mail.entity.UmsUserInvite;


/**
 * <p>
 * app用户邀请表 服务类
 * </p>
 *
 * @author 070
 * @since 2021-11-09
 */
public interface UmsUserInviteManageService extends IService<UmsUserInvite> {

    Page<UmsUserInvite> getPcInviteUser(String userName,String parentName);
}
