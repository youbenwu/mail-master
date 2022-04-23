package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.OmsIntegralOrder;

import java.util.List;

/**
 * <p>
 * 积分兑换订单表 服务类
 * </p>
 *
 * @author 070
 * @since 2021-11-17
 */
public interface OmsIntegralOrderService extends IService<OmsIntegralOrder> {
    /**
     * 积分兑换记录
     * @param integralOrderId id
     * @return 返回值
     */
    List<OmsIntegralOrder> getAllIntegralOrder(Long integralOrderId);
}
