package com.ys.mail.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.config.RedisConfig;
import com.ys.mail.entity.SmsProductStore;
import com.ys.mail.entity.UmsUser;
import com.ys.mail.exception.ApiAssert;
import com.ys.mail.exception.code.CommonResultCode;
import com.ys.mail.mapper.SmsProductStoreMapper;
import com.ys.mail.model.admin.param.SmsProductStoreParam;
import com.ys.mail.model.admin.query.SmsProductStoreQuery;
import com.ys.mail.model.admin.vo.SmsProductStoreVO;
import com.ys.mail.service.RedisService;
import com.ys.mail.service.SmsProductStoreService;
import com.ys.mail.service.UserManageService;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.PcUserUtil;
import com.ys.mail.wrapper.SqlQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户_商品_店铺表 服务实现类
 * </p>
 *
 * @author 070
 * @since 2022-04-25
 */
@Service
public class SmsProductStoreServiceImpl extends ServiceImpl<SmsProductStoreMapper, SmsProductStore> implements SmsProductStoreService {

    @Autowired
    private SmsProductStoreMapper smsProductStoreMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private RedisConfig redisConfig;
    @Autowired
    private UserManageService userManageService;

    @Override
    public IPage<SmsProductStoreVO> getPage(SmsProductStoreQuery query) {
        IPage<SmsProductStoreVO> page = new Page<>(query.getPageNum(), query.getPageSize());
        SqlQueryWrapper<SmsProductStoreVO> wrapper = new SqlQueryWrapper<>();

        wrapper.like("t1.store_name", query.getStoreName())
               .like("t1.store_phone", query.getStorePhone())
               .eq("t1.review_state", query.getReviewState())
               .compareDate("t1.create_time", query.getBeginTime(), query.getEndTime())
               .orderByDesc("t1.create_time");

        return smsProductStoreMapper.getPage(page, wrapper);
    }

    @Override
    public boolean updateReviewState(SmsProductStoreParam param) {
        // 店铺ID校验
        Long pdtStoreId = param.getPdtStoreId();
        SmsProductStore smsProductStore = this.getById(pdtStoreId);
        ApiAssert.noValue(smsProductStore, CommonResultCode.ID_NO_EXIST);
        // 状态校验
        Integer reviewState = smsProductStore.getReviewState();
        ApiAssert.noEq(SmsProductStore.ReviewState.ZERO.key(), reviewState, CommonResultCode.ILLEGAL_REQUEST);
        // 店铺信息填充
        smsProductStore.setReviewState(param.getReviewState());
        smsProductStore.setReviewDesc(param.getReviewDesc());
        smsProductStore.setPcUserId(PcUserUtil.getCurrentUser().getPcUserId());
        // 删除用户缓存
        Long userId = smsProductStore.getUserId();
        UmsUser umsUser = userManageService.getById(userId);
        if (BlankUtil.isNotEmpty(umsUser)) {
            redisService.del(redisConfig.fullKey("ums:user:" + umsUser.getPhone()));
        }
        // 店铺修改
        return this.updateById(smsProductStore);
    }
}
