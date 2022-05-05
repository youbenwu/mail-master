package com.ys.mail.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.PmsProductCategory;
import com.ys.mail.model.admin.query.MapQuery;
import com.ys.mail.model.dto.CgyProductDTO;
import com.ys.mail.model.dto.NavCategoryDTO;
import com.ys.mail.model.dto.SearchProductDTO;
import com.ys.mail.model.query.CategorySearchQuery;
import com.ys.mail.model.query.CgyProductQuery;
import com.ys.mail.model.query.PageQuery;
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
     *
     * @return 返回值
     */
    List<ProductCategoryTree> getCategoryTree();

    /**
     * 分类搜索
     *
     * @param query 查询参数
     * @return 返回值
     */
    Page<SearchProductDTO> search(CategorySearchQuery query);

    /**
     * 获取导航分类
     *
     * @return 分类列表
     */
    List<NavCategoryDTO> getNavCategory();

    /**
     * 获取下级导航分类
     *
     * @param parentId 分类ID
     * @return 分类列表
     */
    List<NavCategoryDTO> getSubNavCategory(Long parentId);

    /**
     * 根据分类ID获取商品列表
     *
     * @param query     业务查询对象
     * @param mapQuery  位置查询对象
     * @param pageQuery 分页查询对象
     * @return 结果
     */
    IPage<CgyProductDTO> getProductById(CgyProductQuery query, MapQuery mapQuery, PageQuery pageQuery);
}
