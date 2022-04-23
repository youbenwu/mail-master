package com.ys.mail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.mail.entity.PmsIntegralProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 积分商品信息表 Mapper 接口
 * </p>
 *
 * @author 070
 * @since 2021-11-17
 */
@Mapper
public interface PmsIntegralProductMapper extends BaseMapper<PmsIntegralProduct> {
    /**
     * 积分商城的兑换商品
     * @param integralPdtId 积分兑换的商品id
     * @return 返回值
     */
    List<PmsIntegralProduct> selectAllIntegralPdt(@Param("integralPdtId") Long integralPdtId);

    /**
     * 修改商品销量
     * @param integralPdtId 主键id
     * @param quantity 数量
     * @return
     */
    boolean updateSaleById(@Param("integralPdtId") Long integralPdtId, @Param("quantity") Integer quantity);
}
