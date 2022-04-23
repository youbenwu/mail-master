package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.PmsProduct;
import com.ys.mail.model.admin.query.PmsProductQuery;
import com.ys.mail.model.dto.ProductInfoDTO;
import com.ys.mail.model.vo.PmsProductVO;

import java.io.File;
import java.io.IOException;

/**
 * <p>
 * 商品信息表 服务类
 * </p>
 *
 * @author 070
 * @since 2021-11-10
 */
public interface PmsProductService extends IService<PmsProduct> {

    Page<PmsProductVO> getPage(PmsProductQuery query);

    /**
     * 商品详情页
     *
     * @param productId 商品id
     * @return 返回值
     */
    ProductInfoDTO getProductInfo(Long productId);

    /**
     * 商品删除
     * @param productId 商品id
     * @return 返回值
     */
    boolean delete(Long productId);
}
