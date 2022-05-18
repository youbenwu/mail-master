package com.ys.mail.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.entity.PmsProductAttribute;
import com.ys.mail.mapper.PmsProductAttributeMapper;
import com.ys.mail.model.vo.PmsProductAttributeVO;
import com.ys.mail.service.PmsProductAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 商品属性参数表 服务实现类
 * </p>
 *
 * @author 070
 * @since 2021-11-18
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class PmsProductAttributeServiceImpl extends ServiceImpl<PmsProductAttributeMapper, PmsProductAttribute> implements PmsProductAttributeService {

    @Autowired
    private PmsProductAttributeMapper pmsProductAttributeMapper;

    @Override
    public IPage<PmsProductAttributeVO> get(IPage<PmsProductAttributeVO> page, String pdtAttrName, String pdtCgyName) {
        return pmsProductAttributeMapper.get(page, pdtAttrName, pdtCgyName);
    }
}
