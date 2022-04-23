package com.ys.mail.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.entity.UmsProductCollect;
import com.ys.mail.mapper.UmsProductCollectMapper;
import com.ys.mail.model.admin.dto.UmsProductCollectDto;
import com.ys.mail.service.UmsProductCollectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 用户收藏产品中间表 服务实现类
 * </p>
 *
 * @author 070
 * @since 2021-12-07
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class UmsProductCollectServiceImpl extends ServiceImpl<UmsProductCollectMapper, UmsProductCollect> implements UmsProductCollectService {

    private final static Logger LOGGER = LoggerFactory.getLogger(UmsProductCollectServiceImpl.class);

    @Autowired
    private UmsProductCollectMapper umsProductCollectMapper;

    public Page<UmsProductCollectDto> getUmsProductCollectList(Page page){
        Page<UmsProductCollectDto> result=umsProductCollectMapper.getUmsProductCollectList(page);
        return result;
    }



}
