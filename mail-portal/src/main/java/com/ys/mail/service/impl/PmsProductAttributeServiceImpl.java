package com.ys.mail.service.impl;

import com.ys.mail.entity.PmsProductAttribute;
import com.ys.mail.service.PmsProductAttributeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.ys.mail.mapper.PmsProductAttributeMapper;
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

}
