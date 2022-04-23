package com.ys.mail.controller;


import com.ys.mail.entity.UmsAddress;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.param.UmsAddressParam;
import com.ys.mail.service.UmsAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户收货地址表 前端控制器
 * </p>
 *
 * @author 070
 * @since 2021-11-15
 */
@RestController
@RequestMapping("/address")
@Validated
@Api(tags = "用户收货地址管理")
public class UmsAddressController {

    @Autowired
    private UmsAddressService addressService;

    @ApiOperation("查询用户收货地址列表")
    @GetMapping(value = "/getAllAddress")
    public CommonResult<List<UmsAddress>> getAllAddress() {
        // 不用传入用户地址进行查询
        List<UmsAddress> addresses = addressService.getAllAddress();
        return CommonResult.success(addresses);
    }

    @ApiOperation("用户删除收货地址")
    @DeleteMapping(value = "/delAddress/{addressId:^\\d{19}$}")
    public CommonResult<Boolean> delAddress(@PathVariable Long addressId) {
        return addressService.delAddress(addressId);
    }

    @ApiOperation("查看收货地址详情")
    @GetMapping(value = "/getAddressInfo/{addressId:^\\d{19}$}")
    public CommonResult<UmsAddress> getAddressInfo(@PathVariable Long addressId) {
        UmsAddress result = addressService.getById(addressId);
        return CommonResult.success(result);
    }

    @ApiOperation("用户设计收货地址是否为默认")
    @PostMapping(value = "/createAsIsDefault/{addressId:^\\d{19}$}")
    public CommonResult<Boolean> createAsIsDefault(@PathVariable Long addressId) {
        return addressService.createAsIsDefault(addressId);
    }

    @ApiOperation("新增和修改收货地址")
    @PostMapping(value = "/createAddress")
    public CommonResult<Boolean> createAddress(@Validated @RequestBody UmsAddressParam param) {

        return addressService.createAddress(param);
    }
}
