package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.PmsProductAttribute;

/**
 * <p>
 * 商品属性参数表 服务类
 * </p>
 *
 * @author 070
 * @since 2021-11-18
 */

public interface PmsProductAttributeService extends IService<PmsProductAttribute> {


    Page<PmsProductAttribute> get(Page page,String productAttributeName);
}
