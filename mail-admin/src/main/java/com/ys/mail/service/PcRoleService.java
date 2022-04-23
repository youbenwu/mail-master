package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.PcRole;
import com.ys.mail.model.admin.param.PcRoleParam;
import com.ys.mail.model.admin.vo.RoleOwnMenuVO;

import java.util.List;

/**
 * <p>
 * 后台角色表 服务类
 * </p>
 *
 * @author 070
 * @since 2021-10-20
 */
public interface PcRoleService extends IService<PcRole> {
    /**
     * 为角色授予菜单
     * @param roleId 角色id
     * @param menuIds 菜单id
     * @return 返回值
     */
    int grantMenu(Long roleId, List<Long> menuIds);

    /**
     * 新增或修改角色
     * @param param 参数对象
     * @return 返回值
     */
    boolean createPcRole(PcRoleParam param);

    /**
     * 后台角色删除
     * @param roleId 角色id
     * @return 返回值
     */
    int delRole(Long roleId);

    /**
     * 获取角色拥有的菜单
     * @param roleId 角色id
     * @return 返回数组
     */
    RoleOwnMenuVO getMenusByRoleId(Long roleId);

}
