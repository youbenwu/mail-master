package com.ys.mail.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ys.mail.entity.PmsProductAttributeCategory;
import com.ys.mail.model.CommonResult;
import com.ys.mail.service.PmsProductAttributeCategoryService;
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
 * 产品属性分类表 前端控制器
 * </p>
 *
 * @author 070
 * @since 2021-11-30
 */
@RestController
@RequestMapping("/pmsProductAttributeCategory")
@Api(tags = "产品属性分类管理")
public class PmsProductAttributeCategoryController {
    @Autowired
    private PmsProductAttributeCategoryService pmsProductAttributeCategoryService;

    @ApiOperation("产品属性分类添加")
    @PutMapping(value = "/add")
    public CommonResult<Boolean> add(@RequestBody PmsProductAttributeCategory pmsProductAttributeCategory) {
        Long id = pmsProductAttributeCategory.getPdtAttributeCgyId();
        pmsProductAttributeCategory.setPdtAttributeCgyId(id.equals(NumberUtils.LONG_ZERO) ? IdWorker.generateId() : id);
        Boolean result = pmsProductAttributeCategoryService.saveOrUpdate(pmsProductAttributeCategory);
        return CommonResult.success(result);
    }

    @ApiOperation("产品属性分类查询")
    @GetMapping(value = "get")
    public CommonResult<Page<PmsProductAttributeCategory>> get(@RequestParam(name = "name", required = false) String name, @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        QueryWrapper<PmsProductAttributeCategory> queryWrapper = new QueryWrapper<>();
        if (!BlankUtil.isEmpty(name)) {
            queryWrapper.like("name", name);
        }
        queryWrapper.orderByDesc("create_time");
        Page<PmsProductAttributeCategory> page = new Page<>(pageNum, pageSize);
        Page<PmsProductAttributeCategory> result = pmsProductAttributeCategoryService.page(page, queryWrapper);
        return CommonResult.success(result);
    }

    @ApiOperation("产品属性分类删除")
    @DeleteMapping(value = "delete")
    public CommonResult<Boolean> delete(@RequestParam(name = "pdtAttributeCgyId", required = true) Long pdtAttributeCgyId) {
        Boolean result = pmsProductAttributeCategoryService.removeById(pdtAttributeCgyId);
        return CommonResult.success(result);
    }

    @ApiOperation("产品属性分类全部查询")
    @GetMapping(value = "getAll")
    public CommonResult<List<PmsProductAttributeCategory>> getAll(@RequestParam(name = "name", required = false) String name) {
        QueryWrapper<PmsProductAttributeCategory> queryWrapper = new QueryWrapper<>();
        if (!BlankUtil.isEmpty(name)) {
            queryWrapper.like("name", name);
        }
        queryWrapper.orderByDesc("create_time");
        List<PmsProductAttributeCategory> result = pmsProductAttributeCategoryService.list(queryWrapper);
        return CommonResult.success(result);
    }
}
