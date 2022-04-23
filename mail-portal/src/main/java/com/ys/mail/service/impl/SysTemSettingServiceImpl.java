package com.ys.mail.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.entity.SysTemSetting;
import com.ys.mail.mapper.SysTemSettingMapper;
import com.ys.mail.service.SysTemSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统参数设置 服务实现类
 * </p>
 *
 * @author 070
 * @since 2021-12-02
 */
@Service
public class SysTemSettingServiceImpl extends ServiceImpl<SysTemSettingMapper, SysTemSetting> implements SysTemSettingService {

    @Autowired
    private SysTemSettingMapper sysTemSettingMapper;

    @Override
    public SysTemSetting getOneByType(Integer temType) {
        return sysTemSettingMapper.getOneByType(temType);
    }
}
