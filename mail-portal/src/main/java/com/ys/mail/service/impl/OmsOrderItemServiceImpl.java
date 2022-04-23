package com.ys.mail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.entity.OmsOrderItem;
import com.ys.mail.mapper.OmsOrderItemMapper;
import com.ys.mail.model.vo.OmsOrderItemSearchVO;
import com.ys.mail.model.vo.OmsOrderItemVO;
import com.ys.mail.model.vo.OrderItemSkuVO;
import com.ys.mail.service.OmsOrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 订单中所包含的商品 服务实现类
 * </p>
 *
 * @author 070
 * @since 2021-11-24
 */
@Service
public class OmsOrderItemServiceImpl extends ServiceImpl<OmsOrderItemMapper, OmsOrderItem> implements OmsOrderItemService {

    @Autowired
    private OmsOrderItemMapper omsOrderItemMapper;

    @Override
    public List<OmsOrderItemVO> getItemList(Long orderId) {
        List<OmsOrderItemVO> result = omsOrderItemMapper.getItemList(orderId);
        return result;
    }

    @Override
    public List<Long> getOrderIdsByProductName(String productName, Long orderId, Integer pageSize) {
        return omsOrderItemMapper.getOrderIdsByProductName(productName, orderId, pageSize);
    }

    @Override
    public List<OmsOrderItemSearchVO> getOmsOrderItemSearchVO(Long orderId) {
        return omsOrderItemMapper.getOmsOrderItemSearchVO(orderId);
    }

    @Override
    public OrderItemSkuVO getByOrderSn(String orderSn) {
        return omsOrderItemMapper.selectByOrderSn(orderSn);
    }

    @Override
    public OmsOrderItem getOneByOrderId(Long orderId) {
        QueryWrapper<OmsOrderItem> wrapper = new QueryWrapper<>();
        wrapper.eq("order_id", orderId).last("LIMIT 1");
        return this.getOne(wrapper);
    }

    @Override
    public Integer getQuantity(Long orderId) {
        return omsOrderItemMapper.getQuantity(orderId);
    }
}
