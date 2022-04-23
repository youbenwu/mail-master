package com.ys.mail.controller;


import com.ys.mail.entity.PmsSkuStock;
import com.ys.mail.model.CommonResult;
import com.ys.mail.service.PmsSkuStockService;
import com.ys.mail.util.IdWorker;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * sku的库存 前端控制器
 * </p>
 *
 * @author 070
 * @since 2021-11-23
 */
@RestController
@RequestMapping("/sku/stock")
@Api(tags = "商品SKU管理")
public class PmsSkuStockController {

    @Autowired
    private PmsSkuStockService skuStockService;

    @ApiOperation("造数据")
    @PostMapping(value = "/saveOrUpdate")
    public CommonResult<Boolean> saveOrUpdate(@Validated @RequestBody PmsSkuStock skuStock) {
        Long skuStockId = skuStock.getSkuStockId();
        skuStock.setSkuStockId(skuStockId.equals(NumberUtils.LONG_ZERO) ? IdWorker.generateId() : skuStockId);
        return skuStockService.saveOrUpdate(skuStock) ? CommonResult.success(true) : CommonResult.failed(false);
    }

}
