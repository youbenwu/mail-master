package com.ys.mail.service.impl;

import com.ys.mail.cache.RongCloudCache;
import com.ys.mail.component.rongcloud.RongCloudClient;
import com.ys.mail.enums.EnumSettingType;
import com.ys.mail.exception.ApiException;
import com.ys.mail.mapper.PcUserMapper;
import com.ys.mail.model.admin.vo.UserImInfoVO;
import com.ys.mail.service.RongCloudService;
import com.ys.mail.service.SysSettingService;
import com.ys.mail.util.BlankUtil;
import io.rong.models.Result;
import io.rong.models.message.MessageModel;
import io.rong.models.response.ResponseResult;
import io.rong.models.response.TokenResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Desc
 * @Author CRH
 * @Create 2022-01-13 15:30
 */
@Service
public class RongCloudServiceImpl implements RongCloudService {

    @Autowired
    private RongCloudClient rongCloudClient;

    @Autowired
    private RongCloudCache rongCloudCache;

    @Autowired
    private PcUserMapper pcUserMapper;

    @Autowired
    private SysSettingService sysSettingService;

    @Override
    public String register(Long userId, String name, String portrait, String forcedRefresh) throws ApiException {
        // 读取redis中是否已注册
        String userToken = rongCloudCache.getUserToken(userId);
        if (BlankUtil.isNotEmpty(userToken) && forcedRefresh.equals("0")) return userToken;

        // 未注册则向融云申请注册
        TokenResult register = rongCloudClient.register(String.valueOf(userId), name, portrait);
        if (BlankUtil.isNotEmpty(register)) {
            String newToken = register.getToken();
            rongCloudCache.setUserToken(userId, newToken);
            return newToken;
        }
        return null;
    }

    @Override
    public Result updateUser(Long userId, String name, String portrait) throws ApiException {
        return null;
    }

    @Override
    public ResponseResult send(MessageModel message) {
        return rongCloudClient.send(message);
    }

    @Override
    public String getAppKey() {
        return rongCloudClient.getAppKey();
    }

    @Override
    public List<UserImInfoVO> getStaff() {
        // 获取系统设置中的客服ID
        String roleId = sysSettingService.getSettingValue(EnumSettingType.zero);
        return pcUserMapper.getStaff(roleId);
    }
}
