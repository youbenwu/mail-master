package com.ys.mail.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ys.mail.entity.PmsBrand;
import com.ys.mail.model.CommonResult;
import com.ys.mail.service.PmsBrandService;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.IdWorker;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 品牌表 前端控制器
 * </p>
 *
 * @author 070
 * @since 2021-11-30
 */
@RestController
@RequestMapping("/pc/pmsBrand")
@Api(tags = "商品品牌管理")
public class PmsBrandController {

    @Autowired
    private PmsBrandService pmsBrandService;

    @ApiOperation("品牌添加")
    @PutMapping(value = "/add")
    public CommonResult<Boolean> add(@RequestBody PmsBrand pmsBrand) {
        Long id = pmsBrand.getBrandId();
        pmsBrand.setBrandId(id.equals(NumberUtils.LONG_ZERO) ? IdWorker.generateId() : id);
        Boolean result = pmsBrandService.saveOrUpdate(pmsBrand);
        return CommonResult.success(result);
    }

    @ApiOperation("品牌查询")
    @GetMapping(value = "get")
    public CommonResult<Page<PmsBrand>> get(@RequestParam(name = "brandName", required = false) String brandName, @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        QueryWrapper<PmsBrand> queryWrapper = new QueryWrapper<>();
        if (!BlankUtil.isEmpty(brandName)) {
            queryWrapper.like("brand_name", brandName);
        }
        Page<PmsBrand> page = new Page<>(pageNum, pageSize);
        Page<PmsBrand> result = pmsBrandService.page(page, queryWrapper);
        return CommonResult.success(result);
    }

    @ApiOperation("品牌删除")
    @DeleteMapping(value = "delete")
    public CommonResult<Boolean> delete(@RequestParam(name = "brandId", required = true) Long brandId) {
        Boolean result = pmsBrandService.removeById(brandId);
        return CommonResult.success(result);
    }

    @ApiOperation("品牌查询全部")
    @GetMapping(value = "getAll")
    public CommonResult<List<PmsBrand>> getAll(@RequestParam(name = "brandName", required = false) String brandName) {
        QueryWrapper<PmsBrand> queryWrapper = new QueryWrapper<>();
        if (!BlankUtil.isEmpty(brandName)) {
            queryWrapper.like("brand_name", brandName);
        }
        List<PmsBrand> result = pmsBrandService.list(queryWrapper);
        return CommonResult.success(result);
    }
}
