package com.ys.mail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.mail.entity.OmsCartItem;
import com.ys.mail.model.dto.BatchBuyProductDTO;
import com.ys.mail.model.po.BuyProductPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

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
}
