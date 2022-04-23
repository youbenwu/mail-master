package com.ys.mail.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ys.mail.entity.PmsProductAttribute;
import com.ys.mail.model.CommonResult;
import com.ys.mail.service.PmsProductAttributeService;
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
 * 商品属性参数表 前端控制器
 * </p>
 *
 * @author 070
 * @since 2021-11-18
 */
@RestController
@RequestMapping("/pc/pmsProductAttribute")
@Api(tags = "商品属性参数表")
public class PmsProductAttributeController {

    @Autowired
    private PmsProductAttributeService pmsProductAttributeService;

    @ApiOperation("商品属性参数添加")
    @PutMapping(value = "/add")
    public CommonResult<Boolean> add(@RequestBody PmsProductAttribute pmsProductAttribute) {
        Long id = pmsProductAttribute.getProductAttributeId();
        pmsProductAttribute.setProductAttributeId(id.equals(NumberUtils.LONG_ZERO) ? IdWorker.generateId() : id);
        Boolean result = pmsProductAttributeService.saveOrUpdate(pmsProductAttribute);
        return CommonResult.success(result);
    }

    @ApiOperation("商品属性参数查询")
    @GetMapping(value = "get")
    public CommonResult<Page<PmsProductAttribute>> get(@RequestParam(name = "productAttributeName", required = false) String productAttributeName, @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
//        QueryWrapper<PmsProductAttribute> queryWrapper = new QueryWrapper<>();
//        if(!BlankUtil.isEmpty(productAttributeName)){
//            queryWrapper.like("product_attribute_name",productAttributeName);
//        }
        Page<PmsProductAttribute> page = new Page<>(pageNum, pageSize);
        Page<PmsProductAttribute> result = pmsProductAttributeService.get(page, productAttributeName);
        return CommonResult.success(result);
    }

    @ApiOperation("商品属性参数删除")
    @DeleteMapping(value = "delete")
    public CommonResult<Boolean> delete(@RequestParam(name = "productAttributeId", required = true) Long productAttributeId) {
        Boolean result = pmsProductAttributeService.removeById(productAttributeId);
        return CommonResult.success(result);
    }

    @ApiOperation("商品属性上级分类查询")
    @GetMapping(value = "getByParentId")
    public CommonResult<List<PmsProductAttribute>> getByParentId(@RequestParam(name = "pdtAttributeCgyId", required = false) String pdtAttributeCgyId) {
        QueryWrapper<PmsProductAttribute> queryWrapper = new QueryWrapper<>();
        if (!BlankUtil.isEmpty(pdtAttributeCgyId)) {
            queryWrapper.eq("pdt_attribute_cgy_id", pdtAttributeCgyId);
        }
        queryWrapper.orderByDesc("create_time");
        List<PmsProductAttribute> result = pmsProductAttributeService.list(queryWrapper);
        return CommonResult.success(result);
    }
}
