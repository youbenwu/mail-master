package com.ys.mail.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ys.mail.exception.code.BusinessErrorCode;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.dto.CgyProductDTO;
import com.ys.mail.model.dto.NavCategoryDTO;
import com.ys.mail.model.dto.SearchProductDTO;
import com.ys.mail.model.query.CategorySearchQuery;
import com.ys.mail.model.query.CgyProductQuery;
import com.ys.mail.model.tree.ProductCategoryTree;
import com.ys.mail.service.PmsProductCategoryService;
import com.ys.mail.util.BlankUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author DT
 * @version 1.0
 * @date 2021-11-15 11:27
 */
@Validated
@RestController
@Api(tags = "商品分类管理")
@RequestMapping("/product/category")
public class PmsProductCategoryController {

    @Autowired
    private PmsProductCategoryService productCategoryService;

    @ApiOperation("商品分类管理列表-DT")
    @GetMapping(value = "/tree")
    public CommonResult<List<ProductCategoryTree>> tree() {
        List<ProductCategoryTree> trees = productCategoryService.getCategoryTree();
        return BlankUtil.isEmpty(trees) ? CommonResult.failed(BusinessErrorCode.NOT_PDT_CATEGORY) : CommonResult.success(trees);
    }

    @ApiOperation("分类搜索-DT")
    @PostMapping(value = "/search")
    public CommonResult<Page<SearchProductDTO>> search(@Validated @RequestBody CategorySearchQuery query) {
        //条件:查找商品，价格升和降,综合查全部,新品查是否是新品,keyword查,翻页条数
        Page<SearchProductDTO> result = productCategoryService.search(query);
        return BlankUtil.isEmpty(result) ? CommonResult.failed(BusinessErrorCode.NOT_PDT_CATEGORY) : CommonResult.success(result);
    }

    @ApiOperation("获取导航分类列表")
    @GetMapping(value = "/getNavCategory")
    public CommonResult<List<NavCategoryDTO>> getNavCategory() {
        List<NavCategoryDTO> navCategory = productCategoryService.getNavCategory();
        return CommonResult.success(navCategory);
    }

    @ApiOperation("根据分类ID查询商品列表")
    @GetMapping(value = "/getProductById")
    public CommonResult<List<CgyProductDTO>> getProductById(CgyProductQuery query) {
        List<CgyProductDTO> list = productCategoryService.getProductById(query);
        return CommonResult.success(list);
    }
}
