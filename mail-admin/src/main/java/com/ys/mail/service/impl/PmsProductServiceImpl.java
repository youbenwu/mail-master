package com.ys.mail.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.entity.PmsProduct;
import com.ys.mail.mapper.PmsProductMapper;
import com.ys.mail.model.admin.query.PmsProductQuery;
import com.ys.mail.model.dto.ProductInfoDTO;
import com.ys.mail.model.vo.PmsProductVO;
import com.ys.mail.service.PmsProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * <p>
 * 商品信息表 服务实现类
 * </p>
 *
 * @author 070
 * @since 2021-11-10
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class PmsProductServiceImpl extends ServiceImpl<PmsProductMapper, PmsProduct> implements PmsProductService {

    private final static Logger LOGGER = LoggerFactory.getLogger(PmsProductServiceImpl.class);

    @Autowired
    private PmsProductMapper pmsProductMapper;

    @Override
    public Page<PmsProductVO> getPage(PmsProductQuery query) {
        Page<PmsProductVO> page = new Page<>(query.getPageNum(), query.getPageSize());
        return pmsProductMapper.getPage(page, query);
    }

    @Override
    public ProductInfoDTO getProductInfo(Long productId) {
        return pmsProductMapper.selectProductInfo(productId);
    }

    @Override
    public boolean delete(Long productId) {
        // 商品删除的同时必须连着sku一起删除,否则置换的时候会出现问题
        return pmsProductMapper.delById(productId);
    }
}
