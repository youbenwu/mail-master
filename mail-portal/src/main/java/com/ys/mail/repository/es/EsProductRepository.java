package com.ys.mail.repository.es;

import com.ys.mail.entity.EsProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface EsProductRepository extends ElasticsearchRepository<EsProduct,Long> {
    Page<EsProduct> findByProductName(String name,Pageable page);
}
