package com.ys.mail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.mail.entity.SysProductLog;
import com.ys.mail.model.dto.SysProductLogDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 商品足迹表 Mapper 接口
 * </p>
 *
 * @author 070
 * @since 2021-11-19
 */
@Mapper
public interface SysProductLogMapper extends BaseMapper<SysProductLog> {
    /**
     * 我的足迹
     * @param userId 用户id
     * @param productLogId 商品日志id
     * @return 返回值
     */
    List<SysProductLogDTO> selectAllProductLog(@Param("userId") Long userId, @Param("productLogId") Long productLogId);
}
