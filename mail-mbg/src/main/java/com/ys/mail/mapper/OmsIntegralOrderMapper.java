package com.ys.mail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.mail.entity.OmsIntegralOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 积分兑换订单表 Mapper 接口
 * </p>
 *
 * @author 070
 * @since 2021-11-17
 */
@Mapper
public interface OmsIntegralOrderMapper extends BaseMapper<OmsIntegralOrder> {
    /**
     * 查询出积分兑换的订单列表
     * @param integralOrderId 主键id
     * @param userId 用户id
     * @return 返回值
     */
    List<OmsIntegralOrder> selectAllIntegralOrder(@Param("integralOrderId") Long integralOrderId, @Param("userId") Long userId);
}
