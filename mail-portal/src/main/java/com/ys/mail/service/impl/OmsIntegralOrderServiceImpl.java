package com.ys.mail.service.impl;

import com.ys.mail.entity.UmsUser;
import com.ys.mail.service.OmsIntegralOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ys.mail.entity.OmsIntegralOrder;
import com.ys.mail.mapper.OmsIntegralOrderMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 积分兑换订单表 服务实现类
 * </p>
 *
 * @author 070
 * @since 2021-11-17
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class OmsIntegralOrderServiceImpl extends ServiceImpl<OmsIntegralOrderMapper, OmsIntegralOrder> implements OmsIntegralOrderService {

    @Autowired
    private OmsIntegralOrderMapper integralOrderMapper;

    @Override
    public List<OmsIntegralOrder> getAllIntegralOrder(Long integralOrderId) {

        UmsUser currentUser = UserUtil.getCurrentUser();
        return integralOrderMapper.selectAllIntegralOrder(integralOrderId,currentUser.getUserId());
    }
}
