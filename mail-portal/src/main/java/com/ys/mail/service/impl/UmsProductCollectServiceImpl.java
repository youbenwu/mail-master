package com.ys.mail.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.entity.UmsIntegral;
import com.ys.mail.entity.UmsProductCollect;
import com.ys.mail.mapper.UmsIntegralMapper;
import com.ys.mail.mapper.UmsProductCollectMapper;
import com.ys.mail.service.UmsIntegralService;
import com.ys.mail.service.UmsProductCollectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author DT
 * @version 1.0
 * @date 2021-12-15 13:26
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class UmsProductCollectServiceImpl extends ServiceImpl<UmsProductCollectMapper, UmsProductCollect> implements UmsProductCollectService {
}
