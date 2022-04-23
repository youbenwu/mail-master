package com.ys.mail.controller;

import com.ys.mail.annotation.LocalLockAnn;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.param.TimeShopParam;
import com.ys.mail.service.SmsFlashPromotionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author DT
 * @version 1.0
 * @date 2021-11-24 16:10
 */
@RestController
@RequestMapping("/flash/promotion")
@Validated
@Api(tags = "商品限时购管理")
public class SmsFlashPromotionController {

    @Autowired
    private SmsFlashPromotionService flashPromotionService;

    @ApiOperation("商品限时购生成订单")
    @PostMapping(value = "/saveTimeShop")
    @LocalLockAnn(key = "saveTimeShop:arg[0]")
    public CommonResult<Boolean> saveTimeShop(@Validated @RequestBody TimeShopParam param) throws Exception {
        // 限时购生成订单
        return flashPromotionService.saveTimeShop(param);
    }

    // 线上发布,线上发货,线上发布就是轮回秒杀,要分开写

    @ApiOperation("线上发布")
    @PostMapping(value = "/savePostedOnline")
    @LocalLockAnn(key = "savePostedOnline:arg[0]")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单id", dataType = "Long", required = true),
            @ApiImplicitParam(name = "productPrice", value = "订单价格", dataType = "Long", required = true)
    })
    public CommonResult<Boolean> savePostedOnline(@RequestParam("orderId") @NotBlank @Pattern(regexp = "^\\d{19}$") String orderId,
                                                  @RequestParam("productPrice") @NotNull Long productPrice) {
        // 线上发布,订单id,
        return flashPromotionService.savePostedOnline(orderId, productPrice);
    }
}
