package com.ys.mail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.mail.entity.PcRole;
import com.ys.mail.entity.PcRole;
import com.ys.mail.model.admin.vo.RoleOwnMenuVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 后台角色表 Mapper 接口
 * </p>
 *
 * @author 070
 * @since 2021-10-20
 */
@Mapper
public interface PcRoleMapper extends BaseMapper<PcRole> {
    /**
     * 根据角色id删除所有的菜单
     * @param roleId
     * @return
     */
    int delRoleMenuByRoleId(@Param("roleId") Long roleId);
    /**
     * 为角色授予菜单
     * @param roleId 角色id
     * @param mapList 菜单集合id和主键id
     * @return 返回值
     */
    int insertRoleMenus(@Param("roleId") Long roleId, @Param("mapList") List<Map<String, Long>> mapList);

    /**
     * 根据角色id删除中间的关联用户id
     * @param roleId 角色id
     * @return 返回值
     */
    int delUserRoleByRoleId(@Param("roleId") Long roleId);

    /**
     * 获取角色拥有的菜单
     * @param roleId 角色id
     * @return 返回值
     */
    String[] getMenusByRoleId(@Param("roleId") Long roleId);
}
