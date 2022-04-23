package com.ys.mail.service.impl;

import com.ys.mail.entity.UmsUser;
import com.ys.mail.service.UmsIntegralService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.service.UmsUserService;
import com.ys.mail.service.UserCacheService;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ys.mail.entity.UmsIntegral;
import com.ys.mail.mapper.UmsIntegralMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 积分变化历史记录表 服务实现类
 * </p>
 *
 * @author 070
 * @since 2021-11-17
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class UmsIntegralServiceImpl extends ServiceImpl<UmsIntegralMapper, UmsIntegral> implements UmsIntegralService {

    @Autowired
    private UserCacheService userCacheService;
    @Autowired
    private UmsUserService userService;
    @Autowired
    private UmsIntegralMapper integralMapper;

    @Override
    public UmsUser getIntegralInfo() {
        // 先从缓存中查找出来,如果缓存穿透，则再从数据库中查找出来
        UmsUser currentUser = UserUtil.getCurrentUser();
        UmsUser user = userCacheService.getUser(currentUser.getPhone());
        if(BlankUtil.isEmpty(user)){
            user = userService.getById(currentUser.getUserId());
        }
        return user;
    }

    @Override
    public List<UmsIntegral> getAllIntegral(Long integralId) {
        UmsUser currentUser = UserUtil.getCurrentUser();
        return integralMapper.selectAllIntegral(integralId,currentUser.getUserId());
    }
}
