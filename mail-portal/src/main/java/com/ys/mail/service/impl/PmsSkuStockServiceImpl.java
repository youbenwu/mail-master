package com.ys.mail.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.entity.PmsSkuStock;
import com.ys.mail.mapper.PmsSkuStockMapper;
import com.ys.mail.model.po.MebSkuPO;
import com.ys.mail.service.PmsSkuStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * sku的库存 服务实现类
 * </p>
 *
 * @author 070
 * @since 2021-11-23
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class PmsSkuStockServiceImpl extends ServiceImpl<PmsSkuStockMapper, PmsSkuStock> implements PmsSkuStockService {

    @Autowired
    private PmsSkuStockMapper skuStockMapper;

    @Override
    public PmsSkuStock getSku(Long productId, String spData) {
        return skuStockMapper.selectBySku(productId, spData);
    }

    @Override
    public boolean updateSale(Long skuStockId, Integer quantity) {
        return skuStockMapper.updateSale(skuStockId, quantity);
    }

    @Override
    public PmsSkuStock closestExchangePrice(Long exchangePrice) {
        return skuStockMapper.closestExchangePrice(exchangePrice);
    }

    @Override
    public MebSkuPO getBySkuId(Long skuStockId) {
        return skuStockMapper.selectBySkuId(skuStockId);
    }


}
