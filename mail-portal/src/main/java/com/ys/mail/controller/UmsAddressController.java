package com.ys.mail.controller;


import com.ys.mail.annotation.BlankOrPattern;
import com.ys.mail.entity.UmsAddress;
import com.ys.mail.enums.RegularEnum;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.param.UmsAddressParam;
import com.ys.mail.service.UmsAddressService;
import com.ys.mail.util.UserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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

    @ApiOperation(value = "获取当前距离最近的收货地址", notes = "当经纬度为空则返回默认的地址")
    @GetMapping(value = "/getRecentAddress")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "lat", value = "纬度，范围：-90~90，小数长度最大10位", dataType = "Double"),
            @ApiImplicitParam(name = "lng", value = "经度，范围：-180~180，小数长度最大10位", dataType = "Double")
    })
    public CommonResult<UmsAddress> getRecentAddress(@RequestParam(value = "lat", required = false)
                                                     @BlankOrPattern(regEnum = RegularEnum.LAT, message = "纬度范围：-90~90") Double lat,
                                                     @RequestParam(value = "lng", required = false)
                                                     @BlankOrPattern(regEnum = RegularEnum.LNG, message = "经度范围：-180~180") Double lng) {
        Long userId = UserUtil.getCurrentUser().getUserId();
        UmsAddress result = addressService.getRecentAddressOrDefault(userId, lat, lng);
        return CommonResult.success(result);
    }
}
