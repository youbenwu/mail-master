package com.ys.mail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.ys.mail.entity.PcMenu;
import com.ys.mail.model.admin.tree.PcMenuTree;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author 070
 * @since 2021-10-20
 */
@Mapper
public interface PcMenuMapper extends BaseMapper<PcMenu> {
    /**
     * 获取当前用户所有可以访问的菜单集合
     * @param pcUserId
     * @return
     */
    List<PcMenu> selectMenuList(@Param("pcUserId") Long pcUserId);

    /**
     * 查询出用户的权限集合
     * @param username 用户名
     * @param pcUserId 用户id
     * @return 返回集合
     */
    List<PcMenuTree> listByUserId(@Param("username") String username, @Param("pcUserId") Long pcUserId);

    /**
     * 查询菜单集合
     * @return 返回菜单集合
     */
    List<PcMenuTree> selectAllTreePcMenu();

    /**
     * 根据菜单id查询出所有关联的用户id
     * @param menuId 菜单id
     * @return 返回集合
     */
    List<String> selectByUserId(@Param("menuId") Long menuId);

    /**
     * 根据菜单id删除相关联的角色id中间表
     * @param menuId 菜单id
     * @return 返回值
     */
    int delRoleMenuByMenuId(@Param("menuId") Long menuId);

    /**
     * 获取所有的菜单集合
     * @return
     */
    List<PcMenuTree> listAllMenu();
}
