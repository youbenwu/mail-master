package com.ys.mail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.entity.UmsAddress;
import com.ys.mail.entity.UmsUser;
import com.ys.mail.exception.code.CommonResultCode;
import com.ys.mail.mapper.UmsAddressMapper;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.query.MapQuery;
import com.ys.mail.model.map.MapDataDTO;
import com.ys.mail.model.param.UmsAddressParam;
import com.ys.mail.service.UmsAddressService;
import com.ys.mail.util.BaiduMapUtil;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.IdWorker;
import com.ys.mail.util.UserUtil;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 用户收货地址表 服务实现类
 * </p>
 *
 * @author 070
 * @since 2021-11-15
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class UmsAddressServiceImpl extends ServiceImpl<UmsAddressMapper, UmsAddress> implements UmsAddressService {

    @Autowired
    private UmsAddressMapper addressMapper;

    @Override
    public List<UmsAddress> getAllAddress() {
        return addressMapper.selectAllAddress(UserUtil.getCurrentUser().getUserId());
    }

    @Override
    public CommonResult<Boolean> delAddress(Long addressId) {
        UmsAddress address = addressMapper.selectById(addressId);
        if (address.getDefaultStatus()) {
            return CommonResult.failed("默认收货地址不能删除");
        }
        boolean b = removeById(addressId);
        return b ? CommonResult.success("success", true) : CommonResult.failed("error", false);
    }

    @Override
    public CommonResult<Boolean> createAsIsDefault(Long addressId) {
        // 是否默认,只能传true,比如单个单个换
        UmsUser user = UserUtil.getCurrentUser();
        QueryWrapper<UmsAddress> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getUserId());
        List<UmsAddress> addresses = addressMapper.selectList(queryWrapper);
        if (BlankUtil.isEmpty(addresses)) {
            return CommonResult.failed("当前用户无任何收货地址");
        }
        addresses.forEach(x -> x.setDefaultStatus(false));
        addressMapper.updateAsIsDefault(addresses);

        // 重新设置默认
        UmsAddress address = UmsAddress.builder()
                                       .addressId(addressId)
                                       .defaultStatus(true)
                                       .build();
        boolean b = updateById(address);
        return b ? CommonResult.success("success", true) : CommonResult.failed("error", false);
    }

    @Override
    public CommonResult<Boolean> createAddress(UmsAddressParam param) {
        UmsAddress address = new UmsAddress();
        BeanUtils.copyProperties(param, address);
        Long addressId = Long.valueOf(param.getAddressId());
        address.setAddressId(Long.valueOf(param.getAddressId()));
        boolean equals = addressId.equals(NumberUtils.LONG_ZERO);
        address.setAddressId(equals ? IdWorker.generateId() : addressId);
        Long userId = UserUtil.getCurrentUser().getUserId();
        List<UmsAddress> addresses = addressMapper.selectList(new QueryWrapper<UmsAddress>().eq("user_id", userId));

        // TODO 暂时先这样,后面在优化
        Boolean defaultStatus = address.getDefaultStatus();
        Integer integerOne = NumberUtils.INTEGER_ONE;
        if (equals) {
            if (BlankUtil.isEmpty(addresses)) {
                address.setDefaultStatus(true);
            }
        } else {
            if (addresses.size() == integerOne && !defaultStatus) {
                return CommonResult.failed(CommonResultCode.ERR_ADDRESS_DEFAULT);
            }
            for (UmsAddress address1 : addresses) {
                if (address1.getAddressId().equals(address.getAddressId())) {
                    if (address1.getDefaultStatus() && !defaultStatus) {
                        return CommonResult.failed(CommonResultCode.ERR_ADDRESS_DEFAULT);
                    }
                }
            }
        }
        if (addresses.size() >= integerOne && defaultStatus) {
            addresses.forEach(x -> x.setDefaultStatus(false));
            addressMapper.updateAsIsDefault(addresses);
            address.setDefaultStatus(true);
        }

        // TODO: 添加收货地址时，需要MQ消息异步调用第三方服务来获取经纬度，并更新字段
        String fullAddress = String.format("%s%s%s%s", address.getProvince(), address.getCity(), address.getCounty(), address.getClientAddress());
        MapDataDTO mapDataDTO = BaiduMapUtil.getLngAndLat(fullAddress);
        if (BlankUtil.isNotEmpty(mapDataDTO)) {
            address.setLatitude(mapDataDTO.getLat());
            address.setLongitude(mapDataDTO.getLng());
        }

        address.setUserId(userId);
        boolean b = saveOrUpdate(address);
        return b ? CommonResult.success(true) : CommonResult.failed(false);
    }


    @Override
    public UmsAddress getByUserId(Long userId) {
        return addressMapper.selectByUserId(userId);
    }

    @Override
    public UmsAddress getRecentAddress(Long userId, MapQuery mapQuery) {
        return addressMapper.selectRecentAddress(userId, mapQuery);
    }

    @Override
    public UmsAddress getRecentAddressOrDefault(Long userId, MapQuery mapQuery) {
        // 获取最近的个人地址
        UmsAddress umsAddress;
        if (BlankUtil.isNotEmpty(mapQuery) &&
                BlankUtil.isNotEmpty(mapQuery.getLat()) &&
                BlankUtil.isNotEmpty(mapQuery.getLng())) {
            umsAddress = this.getRecentAddress(userId, mapQuery);
        } else {
            // 经纬度为空则返回默认的
            umsAddress = this.getByUserId(userId);
        }
        return umsAddress;
    }
}
