package com.ys.mail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.entity.PmsProduct;
import com.ys.mail.entity.PmsProductCategory;
import com.ys.mail.mapper.PmsProductCategoryMapper;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.param.ProductCategoryParam;
import com.ys.mail.model.admin.tree.PcProductCategoryTree;
import com.ys.mail.service.PmsProductCategoryService;
import com.ys.mail.service.PmsProductService;
import com.ys.mail.service.RedisService;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.IdWorker;
import com.ys.mail.util.TreeUtil;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 产品分类表 服务实现类
 * </p>
 *
 * @author 070
 * @since 2021-11-03
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class PmsProductCategoryServiceImpl extends ServiceImpl<PmsProductCategoryMapper, PmsProductCategory> implements PmsProductCategoryService {

    @Autowired
    private PmsProductCategoryMapper pmsProductCategoryMapper;

    @Autowired
    private PmsProductService productService;

    @Value("${redis.database}")
    private String redisDatabase;
    @Value("${redis.key.pdtCategory}")
    private String redisKeyPdtCategory;
    @Autowired
    private RedisService redisService;

    @Override
    public List<PcProductCategoryTree> tree() {
        List<PcProductCategoryTree> trees = pmsProductCategoryMapper.selectTree();
        trees = TreeUtil.toTree(trees, "pdtCgyId", "parentId", "children", PcProductCategoryTree.class);
        return trees;
    }

    @Override
    public boolean create(ProductCategoryParam param) {
        PmsProductCategory productCategory = new PmsProductCategory();
        BeanUtils.copyProperties(param, productCategory);
        Long pdtCgyId = productCategory.getPdtCgyId();
        productCategory.setPdtCgyId(pdtCgyId.equals(NumberUtils.LONG_ZERO) ? IdWorker.generateId() : pdtCgyId);
        boolean saveOrUpdate = saveOrUpdate(productCategory);
        // 清理缓存
        if (saveOrUpdate) {
            String key = redisDatabase + ":" + redisKeyPdtCategory;
            redisService.del(key);
        }
        return saveOrUpdate;
    }

    @Override
    public CommonResult<Boolean> delete(Long pdtCgyId) {
        // 查询商品分类表是否有该分类
        PmsProductCategory productCategory = this.getById(pdtCgyId);
        if (BlankUtil.isNotEmpty(productCategory)) {
            // 首先需要判断该分类的是否有子级，有的话不能删除，如果没有则继续下一步
            QueryWrapper<PmsProductCategory> categoryQueryWrapper = new QueryWrapper<>();
            categoryQueryWrapper.eq("parent_id", pdtCgyId);
            int count = this.count(categoryQueryWrapper);
            if (count > 0) return CommonResult.failed("删除失败：该分类的存在下级分类");

            // 再查看商品分类被多少商品引用过，有的话不能删除（只统计商品已逻辑删除的数量）
            QueryWrapper<PmsProduct> productQueryWrapper = new QueryWrapper<>();
            productQueryWrapper.eq("pdt_cgy_id", pdtCgyId);
            count = productService.count(productQueryWrapper);

            if (count > 0) return CommonResult.failed("删除失败：该分类的下还有商品记录");

            // 开始删除
            boolean remove = this.removeById(pdtCgyId);
            if (remove) {
                // 清理缓存
                String key = redisDatabase + ":" + redisKeyPdtCategory;
                redisService.del(key);
                return CommonResult.success("删除成功", Boolean.TRUE);
            }
            return CommonResult.failed("删除失败");

        }
        return CommonResult.failed("删除失败：商品分类不存在");
    }

}
