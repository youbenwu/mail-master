package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.SysTemSetting;

/**
 * <p>
 * 系统参数设置 服务类
 * </p>
 *
 * @author 070
 * @since 2021-12-02
 */
public interface SysTemSettingService extends IService<SysTemSetting> {

    SysTemSetting getOneByType(Integer temType);
}
