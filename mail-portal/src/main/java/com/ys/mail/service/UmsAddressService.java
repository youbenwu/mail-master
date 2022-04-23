package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.UmsAddress;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.param.UmsAddressParam;

import java.util.List;

/**
 * <p>
 * 用户收货地址表 服务类
 * </p>
 *
 * @author 070
 * @since 2021-11-15
 */
public interface UmsAddressService extends IService<UmsAddress> {
    /**
     * 查询用户收货地址列表
     * @return 返回收货地址集合
     */
    List<UmsAddress> getAllAddress();

    /**
     * 用户删除收货地址
     * @param addressId 收货地址id
     * @return 返回值
     */
    CommonResult<Boolean> delAddress(Long addressId);

    /**
     * 用户设计收货地址是否为默认
     * @param addressId 收货地址id
     * @return 返回值
     */
    CommonResult<Boolean> createAsIsDefault(Long addressId);

    /**
     * 新增和修改收货地址
     * @param param 实体对象
     * @return 返回值
     */
    CommonResult<Boolean> createAddress(UmsAddressParam param);

    /**
     * 获取当前用户的默认收货地址
     * @param userId 用户id
     * @return 返回值
     */
    UmsAddress getByUserId(Long userId);
}
