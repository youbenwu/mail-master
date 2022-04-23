package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.SysTemSetting;
import com.ys.mail.model.CommonResult;

import java.util.List;

/**
 * <p>
 * 系统设置表 服务
 * </p>
 *
 * @author 070
 * @since 2021-11-10
 */
public interface PcSysTemSettingService extends IService<SysTemSetting> {

    CommonResult<Boolean> addBatch(List<SysTemSetting> list);

    CommonResult<Boolean> delete(Long temSettingId);
}
