package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.GroupBuy;
import com.ys.mail.entity.PmsProductCategory;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.dto.SearchProductDTO;
import com.ys.mail.model.query.CategorySearchQuery;
import com.ys.mail.model.tree.ProductCategoryTree;

import java.util.List;

/**
 * @author DT
 * @version 1.0
 * @date 2021-11-15 11:25
 */

public interface PmsProductCategoryService extends IService<PmsProductCategory> {
    /**
     * 商品分类管理列表
     * @return 返回值
     */
    List<ProductCategoryTree> getCategoryTree();

    /**
     * 分类搜索
     * @param query 查询参数
     * @return 返回值
     */
    Page<SearchProductDTO> search(CategorySearchQuery query);
}
