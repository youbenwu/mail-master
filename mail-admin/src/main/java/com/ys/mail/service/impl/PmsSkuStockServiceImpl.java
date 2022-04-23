package com.ys.mail.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.entity.PmsProduct;
import com.ys.mail.entity.PmsSkuStock;
import com.ys.mail.mapper.PmsSkuStockMapper;
import com.ys.mail.service.PmsSkuStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    private PmsSkuStockMapper pmsSkuStockMapper;
    @Override
    public Page<PmsSkuStock> get(String productName, int pageNum, int pageSize) {
        Page<PmsProduct> page = new Page<>(pageNum,pageSize);
        Page<PmsSkuStock> result= pmsSkuStockMapper.get(page,productName);
        return result;
    }
}
