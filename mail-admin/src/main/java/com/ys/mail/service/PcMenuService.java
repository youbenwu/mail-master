package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.PcMenu;
import com.ys.mail.model.admin.param.PcMenuParam;
import com.ys.mail.model.admin.tree.PcMenuTree;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author 070
 * @since 2021-10-20
 */
public interface PcMenuService extends IService<PcMenu> {
    /**
     * 获取当前用户所有可以访问的菜单集合
     * @param pcUserId 用户id
     * @return 返回菜单集合
     */
    List<PcMenu> getMenuList(Long pcUserId);

    /**
     * 查询出用户的权限集合
     * @param username 用户名
     * @param pcUserId 用户id
     * @return 返回树集合
     */
    List<PcMenuTree> listByUserId(String username, Long pcUserId);

    /**
     * 查询菜单集合
     * @return 返回菜单集合
     */
    List<PcMenuTree> getAllTreePcMenu();

    /**
     * 后台菜单新增和修改
     * @param param 实体对象
     * @return 返回值
     */
    boolean createPcMenu(PcMenuParam param);

    /**
     * 后台菜单删除
     * @param menuId 菜单id
     * @return 返回值
     */
    int delPcMenu(Long menuId);

    /**
     * 根据菜单id查询出所有关联的用户id
     * @param menuId 菜单id
     * @return 返回集合
     */
    List<String> getByUserId(Long menuId);

    /**
     * 获取所有的菜单
     * @return
     */
    List<PcMenuTree> listAllMenu();
}
