package com.ys.mail.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.PmsProductAttribute;
import com.ys.mail.model.vo.PmsProductAttributeVO;

/**
 * <p>
 * 商品属性参数表 服务类
 * </p>
 *
 * @author 070
 * @since 2021-11-18
 */

public interface PmsProductAttributeService extends IService<PmsProductAttribute> {

    /**
     * 分页查询
     *
     * @param page        分页
     * @param pdtAttrName 属性名称
     * @param pdtCgyName  属性分类名称
     * @return 结果
     */
    IPage<PmsProductAttributeVO> get(IPage<PmsProductAttributeVO> page, String pdtAttrName, String pdtCgyName);
}
