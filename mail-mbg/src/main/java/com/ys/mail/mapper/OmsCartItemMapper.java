package com.ys.mail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.mail.entity.OmsCartItem;
import com.ys.mail.model.po.BuyProductPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
}
