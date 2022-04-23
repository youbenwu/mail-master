package com.ys.mail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.mail.entity.UmsIntegral;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 积分变化历史记录表 Mapper 接口
 * </p>
 *
 * @author 070
 * @since 2021-11-17
 */
@Mapper
public interface UmsIntegralMapper extends BaseMapper<UmsIntegral> {
    /**
     * 积分明细
     * @param integralId 积分明细id
     * @param userId 用户id
     * @return 返回值
     */
    List<UmsIntegral> selectAllIntegral(@Param("integralId") Long integralId, @Param("userId") Long userId);
}
