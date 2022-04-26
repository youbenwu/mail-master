package com.ys.mail.controller;


import com.ys.mail.annotation.LocalLockAnn;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.param.insert.ProductStoreInsertParam;
import com.ys.mail.model.vo.ProductStoreVO;
import com.ys.mail.service.SmsProductStoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户_商品_店铺表 前端控制器
 * </p>
 *
 * @author 070
 * @since 2022-04-24
 */
@RestController
@Api(tags = "用户商品店铺管理")
@RequestMapping("/product/store")
public class SmsProductStoreController {

    @Autowired
    private SmsProductStoreService smsProductStoreService;

    @ApiOperation("查询个人店铺信息")
    @GetMapping(value = "/")
    public CommonResult<ProductStoreVO> getInfo() {
        ProductStoreVO vo = smsProductStoreService.getInfo();
        return CommonResult.success(vo);
    }

    @ApiOperation("添加个人店铺地址")
    @PostMapping(value = "/add")
    @LocalLockAnn(key = "addStore:arg[0]", expire = 10)
    public CommonResult<Boolean> addStore(@Validated @RequestBody ProductStoreInsertParam param) {
        boolean result = smsProductStoreService.addStore(param);
        return CommonResult.success(result);
    }

    @ApiOperation("修改个人店铺地址")
    @PostMapping(value = "/update")
    public CommonResult<Boolean> updateStore(@Validated @RequestBody ProductStoreInsertParam param) {
        boolean result = smsProductStoreService.updateStore(param);
        return CommonResult.success(result);
    }
}
