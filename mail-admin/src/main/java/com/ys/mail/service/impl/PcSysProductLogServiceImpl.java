package com.ys.mail.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.entity.SysProductLog;
import com.ys.mail.mapper.SysProductLogMapper;
import com.ys.mail.model.dto.SysProductLogDTO;
import com.ys.mail.service.PcSysProductLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Desc
 * @Author CRH
 * @Create 2021-12-07 11:36
 * @Email 18218292802@163.com
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class PcSysProductLogServiceImpl extends ServiceImpl<SysProductLogMapper, SysProductLog> implements PcSysProductLogService {

    final
    private SysProductLogMapper sysProductLogMapper;

    @Override
    public List<SysProductLogDTO> selectAllProductLog(Long userId, Long productLogId) {
        return sysProductLogMapper.selectAllProductLog(userId, productLogId);
    }
}
