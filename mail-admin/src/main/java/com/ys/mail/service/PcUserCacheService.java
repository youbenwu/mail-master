package com.ys.mail.service;

import com.ys.mail.entity.PcMenu;
import com.ys.mail.entity.PcUser;

import java.util.List;

/**
 * @version 1.0
 * @author： DT
 * @date： 2021-10-20 13:31
 */

public interface PcUserCacheService {
    /**
     * 根据用户民获取用户对象
     * @param username 用户名
     * @return 返回用户对象
     */
    PcUser getUser(String username);

    /**
     * 设置用户信息
     * @param user 用户对象
     */
    void setUser(PcUser user);

    /**
     * 获取缓存后台的资源列表
     * @param pcUserId 用户id
     * @return 返回集合
     */
    List<PcMenu> getResourceList(Long pcUserId);

    /**
     * 设置后台后台用户资源列表
     * @param pcUserId 用户id
     * @param menus 返回集合
     */
    void setMenuList(Long pcUserId, List<PcMenu> menus);

    /**
     * 删除后台用户资源列表缓存
     * @param pcUserId 用户id
     */
    void delMenuList(Long pcUserId);
    /**
     * 删除所有后台用户的缓存
     * @param roleId 角色id
     */
    void delRoleMenuList(Long roleId);

    /**
     *当资源信息改变时，删除资源项目后台用户缓存
     * @param menuId 菜单id
     */
    void delMenuListByResource(Long menuId);

    /**
     * 根据用户id删除缓存
     * @param pcUserId 用户id
     */
    void delUser(Long pcUserId);

    /**
     * 设置token的过期时间存入redis缓存中
     * @param authToken token
     */
    void setTokenExpiration(String authToken);

    /**
     * 删除用户的token
     * @param authToken token
     * @return 返回true和false
     */
    boolean delToken(String authToken);
}
