package com.ys.mail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ys.mail.entity.PmsSkuStock;
import com.ys.mail.model.po.MebSkuPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * sku的库存 Mapper 接口
 * </p>
 *
 * @author 070
 * @since 2021-11-23
 */
@Mapper
public interface PmsSkuStockMapper extends BaseMapper<PmsSkuStock> {
    /**
     * 单个查询sku的属性
     *
     * @param productId 商品id
     * @param spData    属性
     * @return 返回值
     */
    PmsSkuStock selectBySku(@Param("productId") Long productId, @Param("spData") String spData);

    Page<PmsSkuStock> get(Page pae, @Param("productName") String productName);

    boolean updateSale(@Param("skuStockId") Long skuStockId, @Param("quantity") Integer quantity);

    PmsSkuStock closestExchangePrice(@Param("exchangePrice") Long exchangePrice);

    /**
     * 查询基本数据
     *
     * @param skuStockId skuId
     * @return 返回值
     */
    MebSkuPO selectBySkuId(@Param("skuStockId") Long skuStockId);
}
