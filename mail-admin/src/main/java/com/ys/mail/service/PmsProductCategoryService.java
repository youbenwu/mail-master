package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.PmsProductCategory;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.param.ProductCategoryParam;
import com.ys.mail.model.admin.tree.PcProductCategoryTree;

import java.util.List;

/**
 * <p>
 * 产品分类表 服务类
 * </p>
 *
 * @author 070
 * @since 2021-11-03
 */
public interface PmsProductCategoryService extends IService<PmsProductCategory> {
    /**
     * 商品分类管理列表
     *
     * @param
     * @return 返回值
     */
    List<PcProductCategoryTree> tree();

    /**
     * 商品分类的创建和修改
     *
     * @param param 参数对象
     * @return 返回值
     */
    boolean create(ProductCategoryParam param);

    CommonResult<Boolean> delete(Long pdtCgyId);
}
