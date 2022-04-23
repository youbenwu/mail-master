package com.ys.mail.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.entity.EsProduct;
import com.ys.mail.repository.es.EsProductRepository;
import com.ys.mail.mapper.EsProductMapper;
import com.ys.mail.service.EsProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Iterator;
import java.util.List;

/**
 * EsProductService接口的实现类
 * Created by zzb on 2019/12/4 11:06
 */
@Service
@Transactional
public class EsProductServiceImpl extends ServiceImpl<EsProductMapper, EsProduct> implements EsProductService {
    private static final Logger logger = LoggerFactory.getLogger(EsProductServiceImpl.class);
    @Autowired
    private EsProductMapper esProductMapper;
    @Autowired
    private EsProductRepository esProductRepository;
    @Override
    public int importAll() {
        List<EsProduct> esProductList = esProductMapper.getProductEs(null);
        Iterable<EsProduct> iterable = esProductRepository.saveAll(esProductList);
        Iterator<EsProduct> iterator = iterable.iterator();
        logger.info("导入ES数据{}:",iterator);
        int count = 0;
        while (iterator.hasNext()) {
            count++;
            iterator.next();
        }
        return count;
    }



    @Override
    public EsProduct create(Long id) {
        List<EsProduct> esProducts = esProductMapper.getProductEs(id);
        if (CollectionUtils.isEmpty(esProducts)) {
            return null;
        }
        EsProduct esProduct = esProducts.get(0);
        logger.info("导入ES单条商品{}:",esProduct);
        return esProductRepository.save(esProduct);
    }

    @Override
    public void delete() {
        esProductRepository.deleteAll();
    }



    @Override
    public Page<EsProduct> searchPage(String keyword, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        return esProductRepository.findByProductName(keyword,pageable);
    }


}