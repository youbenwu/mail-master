package com.ys.mail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.mail.entity.UmsAddress;
import com.ys.mail.model.admin.query.MapQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户收货地址表 Mapper 接口
 * </p>
 *
 * @author 070
 * @since 2021-11-15
 */
@Mapper
public interface UmsAddressMapper extends BaseMapper<UmsAddress> {
    /**
     * 修改当前用户的所有收货地址为false
     *
     * @param addresses 集合
     * @return 返回值
     */
    int updateAsIsDefault(@Param("addresses") List<UmsAddress> addresses);

    /**
     * 获取当前用户的默认收货地址
     *
     * @param userId 用户id
     * @return 返回值
     */
    UmsAddress selectByUserId(@Param("userId") Long userId);

    /**
     * 获取当前用户的最近的收货地址
     *
     * @param userId   用户ID
     * @param mapQuery 经纬度
     * @return 最近的地址，没有则为空
     */
    UmsAddress selectRecentAddress(@Param("userId") Long userId, @Param("mapQuery") MapQuery mapQuery);

    /**
     * 查询用户收货地址列表
     *
     * @param userId 用户id
     * @return 返回收货地址集合
     */
    List<UmsAddress> selectAllAddress(@Param("userId") Long userId);

}
