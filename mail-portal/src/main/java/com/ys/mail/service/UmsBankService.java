package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.UmsBank;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.param.UmsBankParam;

import java.util.List;

/**
 * <p>
 * 用户银行卡表,建议后台转1分审核是否成功 服务类
 * </p>
 *
 * @author 070
 * @since 2021-11-22
 */
public interface UmsBankService extends IService<UmsBank> {
    /**
     * 新增银行卡
     * @param param 实体对象
     * @return 返回值
     */
    CommonResult<Boolean> saveBank(UmsBankParam param);

    /**
     * 获取当前用户的所有银行卡
     * @return 返回值
     */
    List<UmsBank> getByUserId();
}
