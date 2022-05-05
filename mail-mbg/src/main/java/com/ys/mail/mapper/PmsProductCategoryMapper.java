package com.ys.mail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ys.mail.entity.PmsProductCategory;
import com.ys.mail.model.admin.query.MapQuery;
import com.ys.mail.model.admin.tree.PcProductCategoryTree;
import com.ys.mail.model.dto.CgyProductDTO;
import com.ys.mail.model.dto.NavCategoryDTO;
import com.ys.mail.model.dto.SearchProductDTO;
import com.ys.mail.model.query.CategorySearchQuery;
import com.ys.mail.model.query.CgyProductQuery;
import com.ys.mail.model.tree.ProductCategoryTree;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

/**
 * <p>
 * 产品分类表 Mapper 接口
 * </p>
 *
 * @author 070
 * @since 2021-11-03
 */
@Mapper
public interface PmsProductCategoryMapper extends BaseMapper<PmsProductCategory> {
    /**
     * 查询所有的列表
     *
     * @return 返回值
     */
    List<PcProductCategoryTree> selectTree();

    /**
     * 查询出一颗分类商品的树
     *
     * @return 返回值
     */
    List<ProductCategoryTree> selectCategoryTree();

    /**
     * 分类搜索
     *
     * @param query 查询参数
     * @param page  翻页
     * @return 返回值
     */
    Page<SearchProductDTO> selectSearch(@Param("page") Page<T> page, @Param("query") CategorySearchQuery query);

    /**
     * 获取导航分类列表
     *
     * @return 结果
     */
    List<NavCategoryDTO> getNavCategory();

    /**
     * 获取子级导航分类列表
     *
     * @param parentId 商品分类ID
     * @return 结果
     */
    List<NavCategoryDTO> getSubNavCategory(@Param("parentId") Long parentId);

    /**
     * 根据分类ID获取商品列表
     *
     * @param page     分页查询条件
     * @param query    查询条件
     * @param mapQuery 位置查询条件
     * @return 结果
     */
    IPage<CgyProductDTO> getProductById(IPage<CgyProductDTO> page, @Param("query") CgyProductQuery query, @Param("mapQuery") MapQuery mapQuery);
}
