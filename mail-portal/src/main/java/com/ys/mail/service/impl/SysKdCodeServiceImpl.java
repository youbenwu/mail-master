package com.ys.mail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.entity.SysKdCode;
import com.ys.mail.mapper.SysKdCodeMapper;
import com.ys.mail.model.CommonResult;
import com.ys.mail.service.RedisService;
import com.ys.mail.service.SysKdCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 快递公司编号表 服务实现类
 * </p>
 *
 * @author 070
 * @since 2021-12-07
 */
@Service
public class SysKdCodeServiceImpl extends ServiceImpl<SysKdCodeMapper, SysKdCode> implements SysKdCodeService {

    @Value("${redis.expire.common}")
    private Long redisExpire;
    @Value("${redis.key.sysKdCode}")
    private String sysKdCode;
    @Autowired
    private SysKdCodeMapper sysKdCodeMapper;
    @Autowired
    private RedisService redisService;

    @Override
    public CommonResult<List<SysKdCode>> getKdCodeList(){
        if(redisService.hasKey(sysKdCode)){
            List<SysKdCode> arr= (List<SysKdCode>) redisService.get(sysKdCode);
            return CommonResult.success(arr);
        }
        List<SysKdCode> list=sysKdCodeMapper.selectList(new QueryWrapper<>());
        redisService.set(sysKdCode,list,redisExpire);
        return CommonResult.success(list);
    }
}
