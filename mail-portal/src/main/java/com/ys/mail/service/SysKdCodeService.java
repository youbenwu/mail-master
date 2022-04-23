package com.ys.mail.service;

import com.ys.mail.entity.SysKdCode;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.model.CommonResult;

import java.util.List;

/**
 * <p>
 * 快递公司编号表 服务类
 * </p>
 *
 * @author 070
 * @since 2021-12-07
 */
public interface SysKdCodeService extends IService<SysKdCode> {

    CommonResult<List<SysKdCode>> getKdCodeList();
}
