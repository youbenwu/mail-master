package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.EsProduct;
import org.springframework.data.domain.Page;

/**
 * @author ghdhj
 */
public interface EsProductService extends IService<EsProduct> {
   /* *
     * 从数据库中导入商品到ES
     * @return*/

    int importAll();

   /* *
     * 根据id创建商品
     * @param id
     * @return*/

    EsProduct create(Long id);

   /* *
     * 根据关键字搜索
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @return*/

    Page<EsProduct> searchPage(String keyword, Integer pageNum, Integer pageSize);

    public void delete();

}
