package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.PmsSkuStock;

import java.util.List;

/**
 * <p>
 * sku的库存 服务类
 * </p>
 *
 * @author 070
 * @since 2021-11-23
 */
public interface PmsSkuStockService extends IService<PmsSkuStock> {

    Page<PmsSkuStock> get(String productName, int pageNum, int pageSize);
}
