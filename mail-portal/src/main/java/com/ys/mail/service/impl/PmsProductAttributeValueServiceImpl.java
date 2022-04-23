package com.ys.mail.service.impl;

import com.ys.mail.service.PmsProductAttributeValueService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.ys.mail.entity.PmsProductAttributeValue;
import com.ys.mail.mapper.PmsProductAttributeValueMapper;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 存储产品参数信息的表 服务实现类
 * </p>
 *
 * @author 070
 * @since 2021-11-18
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class PmsProductAttributeValueServiceImpl extends ServiceImpl<PmsProductAttributeValueMapper, PmsProductAttributeValue> implements PmsProductAttributeValueService {

}
