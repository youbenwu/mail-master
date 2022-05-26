package com.ys.mail.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ys.mail.entity.PmsSkuStock;
import com.ys.mail.model.CommonResult;
import com.ys.mail.service.PmsSkuStockService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * sku的库存 前端控制器
 * </p>
 *
 * @author 070
 * @since 2021-11-23
 */
@RestController
@RequestMapping("pc/sku/stock")
@Api(tags = "商品SKU管理")
public class PmsSkuStockController {
    @Autowired
    private PmsSkuStockService pmsSkuStockService;

    @ApiOperation("商品SKU添加")
    @PutMapping(value = "/add")
    public CommonResult<Boolean> add(@RequestBody PmsSkuStock pmsSkuStock) {
        boolean result = pmsSkuStockService.addOrUpdate(pmsSkuStock);
        return CommonResult.success(result);
    }

    @ApiOperation("商品SKU查询")
    @GetMapping(value = "get")
    public CommonResult<Page<PmsSkuStock>> get(@RequestParam(name = "productName", required = false) String productName, @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        Page<PmsSkuStock> result = pmsSkuStockService.get(productName, pageNum, pageSize);
        return CommonResult.success(result);
    }

    @ApiOperation("商品SKU删除")
    @DeleteMapping(value = "delete")
    public CommonResult<Boolean> delete(@RequestParam(name = "skuStockId", required = true) Long skuStockId) {
        Boolean result = pmsSkuStockService.removeById(skuStockId);
        return CommonResult.success(result);
    }
}
