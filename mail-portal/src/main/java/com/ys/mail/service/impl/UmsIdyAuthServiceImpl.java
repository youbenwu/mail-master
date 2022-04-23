package com.ys.mail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ys.mail.entity.UmsIdyAuth;
import com.ys.mail.entity.UmsUser;
import com.ys.mail.mapper.UmsIdyAuthMapper;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.param.IdyAuthParam;
import com.ys.mail.service.UmsIdyAuthService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.IdWorker;
import com.ys.mail.util.UserUtil;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 用户身份认证 服务实现类
 * </p>
 *
 * @author 070
 * @since 2021-11-22
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class UmsIdyAuthServiceImpl extends ServiceImpl<UmsIdyAuthMapper, UmsIdyAuth> implements UmsIdyAuthService {

    @Override
    public CommonResult<Boolean> createIdy(IdyAuthParam param) {
        // 完成拷贝
        UmsIdyAuth idyAuth = new UmsIdyAuth();
        BeanUtils.copyProperties(param,idyAuth);
        Long idyAuthId = param.getIdyAuthId();
        boolean equals = idyAuthId.equals(NumberUtils.LONG_ZERO);
        idyAuth.setIdyAuthId(equals ? IdWorker.generateId() : idyAuthId);

        // 查看是否是已认证
        UmsUser currentUser = UserUtil.getCurrentUser();
        Long userId = currentUser.getUserId();
        UmsIdyAuth one = getOne(new QueryWrapper<UmsIdyAuth>().eq("user_id", userId));
        idyAuth.setUserId(userId);
        boolean empty = BlankUtil.isEmpty(one);
        if(empty && equals){
            save(idyAuth);
        }else if(!empty && idyAuthId.equals(idyAuth.getIdyAuthId())){
            updateById(idyAuth);
        }else{
            return CommonResult.failed("格式错误");
        }
        return CommonResult.success(true);
    }
}
