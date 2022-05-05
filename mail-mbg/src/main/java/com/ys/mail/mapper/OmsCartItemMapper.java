package com.ys.mail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.mail.entity.OmsCartItem;
import com.ys.mail.entity.OmsOrder;
import com.ys.mail.entity.OmsOrderItem;
import com.ys.mail.entity.UmsAddress;
import com.ys.mail.model.dto.BatchBuyProductDTO;
import com.ys.mail.model.param.CreateOrderParam;
import com.ys.mail.model.po.BuyProductPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 购物车表 Mapper 接口
 * </p>
 *
 * @author 070
 * @since 2021-11-16
 */
@Mapper
public interface OmsCartItemMapper extends BaseMapper<OmsCartItem> {

    /**
     * 购物车商品修改
     * @param cartItemId 购物车id
     * @param quantity 数量
     * @return 返回值
     */
    boolean updateQuantity(@Param("cartItemId") Long cartItemId, @Param("quantity") Integer quantity);

    /**
     * 查询基本信息
     * @param id id
     * @return 返回值
     */
    OmsCartItem selectByCartInfo(@Param("id") Long id,@Param("quantity") Integer quantity);

    /**
     * 查询出当前用户的购物车信息
     * @param userId 用户id
     * @return 返回值
     */
    List<OmsCartItem> selectList(@Param("userId") Long userId);

    /**
     * 批量删除
     * @param ids 集合id
     * @param userId 用户id
     * @return 返回值
     */
    boolean removeBySkuId(@Param("ids") List<Long> ids,@Param("userId") Long userId);

    /**
     * 批量查询,先userId,直接定位索引
     * @param ids 集合id
     * @param userId 用户id
     * @return 返回值
     */
    List<BuyProductPO> batchProduct(@Param("ids") List<Long> ids,@Param("userId") Long userId);

    /**
     * 查询库存
     * @param skuId id
     * @param num 数量
     * @return 返回值
     */
    Integer selectByNum(@Param("skuId") Long skuId,@Param("num") Integer num);

    /**
     * 修改库存
     * @param skuId skuId
     * @param num 数量
     * @param userId 用户id
     * @return 返回值
     */
    boolean update(@Param("skuId") Long skuId,@Param("num") Integer num,@Param("userId") Long userId);

    /**
     * 新增或修改
     * @param item 参数对象
     * @return 返回值
     */
    boolean insertOrUpdate(@Param("item") OmsCartItem item);

    /**
     * 根据skuId查询出购物车的基本信息
     * @param carts 集合
     * @param userId 用户id
     * @return
     */
    List<OmsCartItem> selectBySkuId(@Param("carts") List<CreateOrderParam.Cart> carts, @Param("userId") Long userId);

    /**
     * 插入购物车订单
     * @param address 地址对象
     * @param items 集合对象
     * @param order 参数
     * @return
     */
    boolean insertOrder(@Param("address") UmsAddress address, @Param("items") List<OmsOrderItem> items, @Param("order") OmsOrder order);
}
