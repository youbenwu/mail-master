package com.ys.mail.controller;


import com.ys.mail.entity.PmsProductCategory;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.param.ProductCategoryParam;
import com.ys.mail.model.admin.tree.PcProductCategoryTree;
import com.ys.mail.service.PmsProductCategoryService;
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
 * 产品分类表 前端控制器
 * </p>
 *
 * @author 070
 * @since 2021-11-03
 */
@RestController
@RequestMapping("/product/category")
@Validated
@Api(tags = "后台商品分类管理")
public class PmsProductCategoryController {

    @Autowired
    private PmsProductCategoryService productCategoryService;


    @ApiOperation("商品分类管理列表-DT")
    @PostMapping(value = "/tree")
    public CommonResult<List<PcProductCategoryTree>> tree() {
        List<PcProductCategoryTree> trees = productCategoryService.tree();
        return CommonResult.success(trees);
    }

    @ApiOperation("商品分类管理详情-DT")
    @GetMapping(value = "/getInfo/{pdtCgyId:^\\d{19}$}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pdtCgyId", value = "商品分类id", dataType = "Long", required = true)
    })
    public CommonResult<PmsProductCategory> getInfo(@PathVariable Long pdtCgyId) {
        //TODO 前后端分离项目,用的是交互,mvc当中用的是model.addAttribute("",)传递参数
        PmsProductCategory result = productCategoryService.getById(pdtCgyId);
        return CommonResult.success(result);
    }

    @ApiOperation("商品分类创建和修改-DT")
    @PostMapping(value = "/create")
    public CommonResult<Boolean> create(@Validated @RequestBody ProductCategoryParam param) {
        boolean b = productCategoryService.create(param);
        return b ? CommonResult.success("success", true) : CommonResult.failed("error", false);
    }

    @ApiOperation("商品分类删除")
    @DeleteMapping(value = "/delete/{pdtCgyId:^\\d{19}$}")
    public CommonResult<Boolean> delete(@PathVariable Long pdtCgyId) {
        return productCategoryService.delete(pdtCgyId);
    }

}
