package com.ys.mail.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ys.mail.entity.PmsProduct;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.query.PmsProductQuery;
import com.ys.mail.model.dto.ProductInfoDTO;
import com.ys.mail.model.vo.PmsProductVO;
import com.ys.mail.service.PmsProductService;
import com.ys.mail.util.IdWorker;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 商品信息表 前端控制器
 * </p>
 *
 * @author 070
 * @since 2021-11-10
 */
@RestController
@RequestMapping("/pc/product")
@Validated
@Api(tags = "商品管理pc端")
public class PmsProductController {

    @Autowired
    private PmsProductService productService;

    @ApiOperation("商品添加")
    @PutMapping(value = "/add")
    public CommonResult<Boolean> add(@RequestBody PmsProduct pmsProduct) {
        Long id = pmsProduct.getProductId();
        pmsProduct.setProductId(id.equals(NumberUtils.LONG_ZERO) ? IdWorker.generateId() : id);
        Boolean result = productService.saveOrUpdate(pmsProduct);
        return CommonResult.success(result);
    }

    @ApiOperation("商品分页查询")
    @GetMapping(value = "/get")
    public CommonResult<Page<PmsProductVO>> get(@Validated PmsProductQuery query) {
        Page<PmsProductVO> result = productService.getPage(query);
        return CommonResult.success(result);
    }

    @ApiOperation("商品删除")
    @DeleteMapping(value = "/delete")
    public CommonResult<Boolean> delete(@RequestParam(name = "productId") @NotNull Long productId) {
        return productService.delete(productId) ? CommonResult.success(true) : CommonResult.failed(false);
    }

    @ApiOperation("商品详情页-DT")
    @GetMapping(value = "/getProductInfo")
    public CommonResult<ProductInfoDTO> getProductInfo(@RequestParam("productId") Long productId) {
        // 商品详情页
        ProductInfoDTO result = productService.getProductInfo(productId);
        return CommonResult.success(result);
    }
}
