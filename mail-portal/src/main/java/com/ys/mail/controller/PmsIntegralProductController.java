package com.ys.mail.controller;


import com.ys.mail.entity.PmsIntegralProduct;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.param.IntegralConvertParam;
import com.ys.mail.service.PmsIntegralProductService;
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
 * 积分商品信息表 前端控制器
 * </p>
 *
 * @author 070
 * @since 2021-11-17
 */
@RestController
@RequestMapping("/integral/product")
@Api(tags = "积分换购商品管理")
public class PmsIntegralProductController {

    @Autowired
    private PmsIntegralProductService integralProductService;

    @ApiOperation("积分商城的兑换商品-DT")
    @GetMapping(value = "/getAllIntegralPdt/{integralPdtId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "integralPdtId", value = "积分换购商品id,用来翻页,首次传0", dataType = "Long", required = true)
    })
    public CommonResult<List<PmsIntegralProduct>> getAllIntegralPdt(@PathVariable Long integralPdtId) {
        List<PmsIntegralProduct> result = integralProductService.getAllIntegralPdt(integralPdtId);
        return CommonResult.success(result);
    }

    @ApiOperation("用户兑换商品-DT")
    @PostMapping(value = "/integralConvertPdt")
    public CommonResult<Boolean> integralConvertPdt(@Validated @RequestBody IntegralConvertParam param) {
        // 商品id,需要多少积分,购买者用户,
        return integralProductService.integralConvertPdt(param);
    }

    @ApiOperation("用户兑换商品详情页-DT")
    @GetMapping(value = "/getIntegralPdtInfo/{integralPdtId:^\\d{19}$}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "integralPdtId", value = "积分换购商品id", dataType = "Long", required = true)
    })
    public CommonResult<PmsIntegralProduct> getIntegralPdtInfo(@PathVariable Long integralPdtId) {
        PmsIntegralProduct result = integralProductService.getById(integralPdtId);
        return CommonResult.success(result);
    }
}
