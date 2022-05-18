package com.ys.mail.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.config.RedisConfig;
import com.ys.mail.entity.SmsHomeAdvertise;
import com.ys.mail.exception.ApiAssert;
import com.ys.mail.exception.code.CommonResultCode;
import com.ys.mail.mapper.SmsHomeAdvertiseMapper;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.param.HomeAdvertiseParam;
import com.ys.mail.model.admin.query.HomeAdvertiseQuery;
import com.ys.mail.service.RedisService;
import com.ys.mail.service.SmsHomeAdvertiseService;
import com.ys.mail.util.IdWorker;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 首页轮播广告表 服务实现类
 * </p>
 *
 * @author 070
 * @since 2021-11-03
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class SmsHomeAdvertiseServiceImpl extends ServiceImpl<SmsHomeAdvertiseMapper, SmsHomeAdvertise> implements SmsHomeAdvertiseService {

    private final static Logger LOGGER = LoggerFactory.getLogger(SmsHomeAdvertiseServiceImpl.class);

    @Autowired
    private SmsHomeAdvertiseMapper homeAdvertiseMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private RedisConfig redisConfig;

    @Override
    public CommonResult<Boolean> create(HomeAdvertiseParam param) {
        // TODO 判断到期时间是否大于过期时间,判断是新增还是修改
        if (param.getStartTime().getTime() >= param.getEndTime().getTime()) {
            return CommonResult.failed("开始时间不能大于结束时间");
        }
        SmsHomeAdvertise homeAdvertise = new SmsHomeAdvertise();
        BeanUtils.copyProperties(param, homeAdvertise);
        Long homeAdvId = homeAdvertise.getHomeAdvId();
        if (homeAdvId.equals(NumberUtils.LONG_ZERO)) {
            homeAdvertise.setHomeAdvId(IdWorker.generateId());
        } else {
            SmsHomeAdvertise smsHomeAdvertise = this.getById(homeAdvId);
            ApiAssert.noValue(smsHomeAdvertise, CommonResultCode.ID_NO_EXIST);
        }
        homeAdvertise.setHomeAdvId(homeAdvId.equals(NumberUtils.LONG_ZERO) ? IdWorker.generateId() : homeAdvId);
        boolean b = saveOrUpdate(homeAdvertise);
        delHomeAdvertise(b);
        return CommonResult.success(b);
    }

    private void delHomeAdvertise(boolean b) {
        if (b) {
            LOGGER.info("清除缓存成功:count{}", redisService.keys(redisConfig.fullKey("home:*")));
        }
    }

    @Override
    public Page<SmsHomeAdvertise> list(HomeAdvertiseQuery query) {
        Page<SmsHomeAdvertise> page = new Page<>(query.getPageNum(), query.getPageSize());
        return homeAdvertiseMapper.selectList(page, query);
    }

    @Override
    public boolean delete(Long homeAdvId) {
        boolean b = removeById(homeAdvId);
        delHomeAdvertise(b);
        return b;
    }
}
