package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.OmsOrderItem;
import com.ys.mail.model.vo.OmsOrderItemSearchVO;
import com.ys.mail.model.vo.OmsOrderItemVO;
import com.ys.mail.model.vo.OrderItemSkuVO;

import java.util.List;

/**
 * <p>
 * 订单中所包含的商品 服务类
 * </p>
 *
 * @author 070
 * @since 2021-11-24
 */
public interface OmsOrderItemService extends IService<OmsOrderItem> {

    List<OmsOrderItemVO> getItemList(Long order);

    /**
     * 根据商品名称查询订单id
     *
     * @param productName 商品名称(可以为空)
     * @param orderId     翻页的订单id(可以为空)
     * @param pageSize    每次查询条数
     * @return 订单id集合
     */
    List<Long> getOrderIdsByProductName(String productName, Long orderId, Integer pageSize);

    List<OmsOrderItemSearchVO> getOmsOrderItemSearchVO(Long orderId);

    /**
     * 查询库存和价格
     *
     * @param orderSn 订单编号
     * @return 返回值
     */
    OrderItemSkuVO getByOrderSn(String orderSn);

    /**
     * 根据订单ID查询一条订单详情
     *
     * @param orderId 订单ID
     * @return 订单详情
     */
    OmsOrderItem getOneByOrderId(Long orderId);

    /**
     * 根据订单id 查询购买数量
     * @param orderId
     * @return
     */
    Integer getQuantity(Long orderId);
}
