package com.ys.mail.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.entity.PmsProductCategory;
import com.ys.mail.mapper.PmsProductCategoryMapper;
import com.ys.mail.model.admin.query.MapQuery;
import com.ys.mail.model.dto.CgyProductDTO;
import com.ys.mail.model.dto.NavCategoryDTO;
import com.ys.mail.model.dto.SearchProductDTO;
import com.ys.mail.model.query.CategorySearchQuery;
import com.ys.mail.model.query.CgyProductQuery;
import com.ys.mail.model.query.PageQuery;
import com.ys.mail.model.tree.ProductCategoryTree;
import com.ys.mail.service.PmsProductCategoryService;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.TreeUtil;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author DT
 * @version 1.0
 * @date 2021-11-15 11:26
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class PmsProductCategoryServiceImpl extends ServiceImpl<PmsProductCategoryMapper, PmsProductCategory> implements PmsProductCategoryService {

    @Autowired
    private PmsProductCategoryMapper productCategoryMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${redis.database}")
    private String redisDatabase;
    @Value("${redis.key.pdtCategory}")
    private String redisKeyPdtCategory;
    @Value("${redis.expire.district}")
    private Long redisExpireDistrict;

    @Override
    public List<ProductCategoryTree> getCategoryTree() {
        String key = redisDatabase
                + ":"
                + redisKeyPdtCategory;
        List<ProductCategoryTree> trees = redisTemplate.opsForList().range(key, 0, 100);
        if (BlankUtil.isEmpty(trees)) {
            trees = TreeUtil.toTree(productCategoryMapper.selectCategoryTree(), "pdtCgyId", "parentId", "children", ProductCategoryTree.class);
            if (!BlankUtil.isEmpty(trees)) {
                redisTemplate.opsForList().rightPushAll(key, trees);
                redisTemplate.expire(key, redisExpireDistrict, TimeUnit.DAYS);
            }
        }
        return trees;
    }

    @Override
    public Page<SearchProductDTO> search(CategorySearchQuery query) {
        Page<T> page = new Page<>(query.getPageNum(), query.getPageSize());
        return productCategoryMapper.selectSearch(page, query);
    }

    @Override
    public List<NavCategoryDTO> getNavCategory() {
        List<NavCategoryDTO> trees = productCategoryMapper.getNavCategory();
        return TreeUtil.toTree(trees, "pdtCgyId", "parentId", "children", NavCategoryDTO.class);
    }

    @Override
    public List<CgyProductDTO> getProductById(CgyProductQuery query) {
        return productCategoryMapper.getProductById(query);
    }

}
