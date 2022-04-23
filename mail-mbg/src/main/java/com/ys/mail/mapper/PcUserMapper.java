package com.ys.mail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.mail.entity.PcUser;
import com.ys.mail.model.admin.vo.UserImInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 后台用户表 Mapper 接口
 * </p>
 *
 * @author 070
 * @since 2021-10-20
 */
@Mapper
public interface PcUserMapper extends BaseMapper<PcUser> {
    /**
     * 根据用户id删除用户角色中间表的关联关系
     *
     * @param pcUserId 用户id
     * @return 返回值
     */
    int delUserRoleByUserId(@Param("pcUserId") Long pcUserId);

    /**
     * 批量插入用户和角色
     *
     * @param pcUserId 用户id
     * @param mapList  角色id集合和主键id集合
     * @return 返回值
     */
    int insertUserRole(@Param("pcUserId") Long pcUserId, @Param("mapList") List<Map<String, Long>> mapList);

    /**
     * 根据角色id查询用户id集合
     *
     * @param roleId 角色id
     * @return 返回集合
     */
    List<String> selectAllUserIdByRoleId(@Param("roleId") Long roleId);

    /**
     * 获取当前用户拥有的角色
     *
     * @param pcUserId 用户id
     * @return 返回值
     */
    String[] getUserOwnRole(@Param("pcUserId") Long pcUserId);

    /**
     * 获取客服列表
     * @param roleId 角色ID
     * @return 客服列表
     */
    List<UserImInfoVO> getStaff(@Param("roleId") String roleId);
}
