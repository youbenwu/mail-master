package com.ys.mail.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.entity.PcRole;
import com.ys.mail.exception.ApiException;
import com.ys.mail.mapper.PcRoleMapper;
import com.ys.mail.model.admin.param.PcRoleParam;
import com.ys.mail.model.admin.tree.PcMenuTree;
import com.ys.mail.model.admin.vo.RoleOwnMenuVO;
import com.ys.mail.service.PcMenuService;
import com.ys.mail.service.PcRoleService;
import com.ys.mail.service.PcUserCacheService;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.IdWorker;
import com.ys.mail.util.TreeUtil;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 后台角色表 服务实现类
 * </p>
 *
 * @author 070
 * @since 2021-10-20
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class PcRoleServiceImpl extends ServiceImpl<PcRoleMapper, PcRole> implements PcRoleService {

    @Autowired
    private PcRoleMapper pcRoleMapper;
    @Autowired
    private PcUserCacheService pcUserCacheService;
    @Autowired
    private PcMenuService menuService;

    @Override
    public int grantMenu(Long roleId, List<Long> menuIds) {
        pcRoleMapper.delRoleMenuByRoleId(roleId);
        pcUserCacheService.delRoleMenuList(roleId);
        List<Map<String, Long>> mapList = new ArrayList<>();
        List<Long> longs = IdWorker.generateIds(menuIds.size());
        for (int i = NumberUtils.INTEGER_ZERO; i < menuIds.size(); i++) {
            Map<String, Long> map = new HashMap<>(16);
            map.put("menuId", menuIds.get(i));
            map.put("roleMenuId", longs.get(i));
            mapList.add(map);
        }
        return pcRoleMapper.insertRoleMenus(roleId, mapList);
    }

    @Override
    public boolean createPcRole(PcRoleParam param) {
        PcRole role = new PcRole();
        BeanUtils.copyProperties(param, role);
        Long roleId = param.getRoleId();
        role.setAllowDelete(true); // 禁止后台修改
        role.setRoleId(roleId.equals(NumberUtils.LONG_ZERO) ? IdWorker.generateId() : roleId);
        return saveOrUpdate(role);
    }

    @Override
    public int delRole(Long roleId) {
        // 查询角色
        PcRole pcRole = this.getById(roleId);
        if (BlankUtil.isEmpty(pcRole)) return NumberUtils.INTEGER_ZERO;
        // 只能删除允许删除的角色
        Boolean allowDelete = pcRole.getAllowDelete();
        if (!allowDelete) return NumberUtils.INTEGER_ZERO;
        // 删除用户角色中间表和菜单角色中间表
        pcRoleMapper.delRoleMenuByRoleId(roleId);
        pcRoleMapper.delUserRoleByRoleId(roleId);
        // 删除缓存
        pcUserCacheService.delRoleMenuList(roleId);
        // 删除角色
        int delete = pcRoleMapper.deleteById(roleId);
        if (delete < 1) throw new ApiException("删除角色异常");
        return delete;
    }

    @Override
    public RoleOwnMenuVO getMenusByRoleId(Long roleId) {
        return RoleOwnMenuVO.builder()
                .arr(pcRoleMapper.getMenusByRoleId(roleId))
                .children(TreeUtil.toTree(menuService.listAllMenu(), "menuId", "parentId", "children", PcMenuTree.class))
                .build();
    }
}
