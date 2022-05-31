package com.ys.mail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.mail.entity.OmsOrderItem;
import com.ys.mail.model.vo.OmsOrderItemSearchVO;
import com.ys.mail.model.vo.OmsOrderItemVO;
import com.ys.mail.model.vo.OrderItemSkuVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 订单中所包含的商品 Mapper 接口
 * </p>
 *
 * @author 070
 * @since 2021-11-24
 */
@Mapper
public interface OmsOrderItemMapper extends BaseMapper<OmsOrderItem> {

    /**
     * 获取订单详情列表
     *
     * @param orderId 订单ID
     * @return 结果
     */
    List<OmsOrderItemVO> getItemList(@Param("orderId") Long orderId);

    List<Long> getOrderIdsByProductName(@Param("productName") String productName, @Param("orderId") Long orderId, @Param("pageSize") Integer pageSize);

    List<OmsOrderItemSearchVO> getOmsOrderItemSearchVO(@Param("orderId") Long orderId);

    /**
     * 查询库存
     *
     * @param orderSn 订单编号
     * @return 返回值
     */
    OrderItemSkuVO selectByOrderSn(@Param("orderSn") String orderSn);

    Integer getQuantity(@Param("orderId") Long orderId);
}
