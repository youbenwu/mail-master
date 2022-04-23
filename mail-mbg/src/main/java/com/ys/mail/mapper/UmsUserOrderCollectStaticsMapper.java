package com.ys.mail.mapper;

import com.ys.mail.model.vo.UmsUserOrderCollectStaticsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Desc 用户订单收藏统计处理
 * @Author CRH
 * @Create 2021-12-07 19:10
 * @Email 18218292802@163.com
 */
@Mapper
public interface UmsUserOrderCollectStaticsMapper {

    // 获取商品收藏等统计信息
    UmsUserOrderCollectStaticsVO getProductCollectStaticsInfo(@Param("userId") Long userId);

    // 获取订单不同状态的统计信息
    UmsUserOrderCollectStaticsVO getOrderStatusStaticsInfo(@Param("userId") Long userId,@Param("cpyType") String cpyType);
}
