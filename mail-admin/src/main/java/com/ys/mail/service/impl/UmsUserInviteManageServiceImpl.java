package com.ys.mail.service.impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.entity.UmsUser;
import com.ys.mail.entity.UmsUserInvite;
import com.ys.mail.mapper.UmsUserInviteMapper;
import com.ys.mail.mapper.UmsUserMapper;
import com.ys.mail.service.UmsUserInviteManageService;
import com.ys.mail.service.UserManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * <p>
 * app用户表 服务实现类
 * </p>
 *
 * @author 070
 * @since 2021-11-09
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class UmsUserInviteManageServiceImpl extends ServiceImpl<UmsUserInviteMapper, UmsUserInvite> implements UmsUserInviteManageService {

    @Autowired
    private UmsUserInviteMapper umsUserInviteMapper;
    @Override
    public Page<UmsUserInvite> getPcInviteUser(String userName, String parentName) {
        //Page<UmsUserInvite>  page= umsUserInviteMapper.getPcInviteUser(userName,parentName);
        return null;
    }
}
