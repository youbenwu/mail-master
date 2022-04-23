package com.ys.mail.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ys.mail.exception.code.BusinessErrorCode;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.dto.SearchProductDTO;
import com.ys.mail.model.query.CategorySearchQuery;
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
@RestController
@RequestMapping("/product/category")
@Validated
@Api(tags = "商品分类管理")
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
}
