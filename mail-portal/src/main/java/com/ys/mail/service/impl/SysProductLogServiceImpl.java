package com.ys.mail.service.impl;

import com.ys.mail.entity.UmsUser;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.dto.SysProductLogDTO;
import com.ys.mail.service.SysProductLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ys.mail.entity.SysProductLog;
import com.ys.mail.mapper.SysProductLogMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 商品足迹表 服务实现类
 * </p>
 *
 * @author 070
 * @since 2021-11-19
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class SysProductLogServiceImpl extends ServiceImpl<SysProductLogMapper, SysProductLog> implements SysProductLogService {

    @Autowired
    private SysProductLogMapper productLogMapper;

    @Override
    public List<SysProductLogDTO> getAllProductLog(Long productLogId) {
        UmsUser currentUser = UserUtil.getCurrentUser();
        return productLogMapper.selectAllProductLog(currentUser.getUserId(),productLogId);
    }

    @Override
    public CommonResult<Boolean> batchDelProductLog(List<Long> ids) {
        boolean b = removeByIds(ids);
        return b ? CommonResult.success(true) : CommonResult.failed(false);
    }
}
