package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.PmsSkuStock;
import com.ys.mail.model.po.MebSkuPO;

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
     * 单个查询sku属性
     *
     * @param productId 商品id
     * @param spData    属性
     * @return 返回值
     */
    PmsSkuStock getSku(Long productId, String spData);

    boolean updateSale(Long skuStockId, Integer quantity);

    /**
     * 查询最接近置换金额的产品
     *
     * @param exchangePrice 置换价格
     * @return
     */
    PmsSkuStock closestExchangePrice(Long exchangePrice);

    /**
     * 查询数据
     *
     * @param skuStockId skuId
     * @return 返回值
     */
    MebSkuPO getBySkuId(Long skuStockId);
}
