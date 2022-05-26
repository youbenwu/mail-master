package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.PmsSkuStock;

/**
 * <p>
 * sku的库存 服务类
 * </p>
 *
 * @author 070
 * @since 2021-11-23
 */
public interface PmsSkuStockService extends IService<PmsSkuStock> {


    /**
     * 添加或更新
     *
     * @param pmsSkuStock 参数
     * @return 操作结果
     */
    boolean addOrUpdate(PmsSkuStock pmsSkuStock);

    /**
     * 分页查询
     *
     * @param productName 商品名称
     * @param pageNum     页码
     * @param pageSize    分页大小
     * @return 列表
     */
    Page<PmsSkuStock> get(String productName, int pageNum, int pageSize);

    /**
     * 检查商品SKU属性是否已存在（针对同一个商品）
     *
     * @param productId 商品ID
     * @param spData    SKU属性值
     * @return 是否已存在，false->表示不存在，true->表示已存在
     */
    boolean isExistsSpData(Long productId, String spData);
}
