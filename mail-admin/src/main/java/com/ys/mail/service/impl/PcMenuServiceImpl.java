package com.ys.mail.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.entity.PcMenu;
import com.ys.mail.mapper.PcMenuMapper;
import com.ys.mail.model.admin.param.PcMenuParam;
import com.ys.mail.model.admin.tree.PcMenuTree;
import com.ys.mail.security.component.DynamicSecurityMetadataSource;
import com.ys.mail.security.component.DynamicSecurityService;
import com.ys.mail.service.PcMenuService;
import com.ys.mail.service.PcUserCacheService;
import com.ys.mail.util.BlankUtil;
import com.ys.mail.util.IdWorker;
import com.ys.mail.util.TreeUtil;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author 070
 * @since 2021-10-20
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class PcMenuServiceImpl extends ServiceImpl<PcMenuMapper, PcMenu> implements PcMenuService {

    @Autowired
    private PcMenuMapper pcMenuMapper;
    @Autowired
    private PcUserCacheService pcUserCacheService;
    @Autowired
    private DynamicSecurityService dynamicSecurityService;

    @Override
    public List<PcMenu> getMenuList(Long pcUserId) {
        return pcMenuMapper.selectMenuList(pcUserId);
    }

    @Override
    public List<PcMenuTree> listByUserId(String username, Long pcUserId) {
        return pcMenuMapper.listByUserId(username, pcUserId);
    }

    @Override
    public List<PcMenuTree> getAllTreePcMenu() {
        return TreeUtil.toTree(listAllMenu(), "menuId", "parentId", "children", PcMenuTree.class);
    }

    @Override
    public boolean createPcMenu(PcMenuParam param) {
        PcMenu menu = new PcMenu();
        BeanUtils.copyProperties(param, menu);
        Long menuId = param.getMenuId();
        menu.setMenuId(menuId.equals(NumberUtils.LONG_ZERO) ? IdWorker.generateId() : menuId);
        boolean flag = saveOrUpdate(menu);
        if (flag) {
            // 动态更新菜单
            DynamicSecurityMetadataSource.setConfigAttributeMap(dynamicSecurityService.loadDataSource());
        }
        return flag;
    }

    @Override
    public int delPcMenu(Long menuId) {
        PcMenu pcMenu = this.getById(menuId);
        if (BlankUtil.isEmpty(pcMenu)) return NumberUtils.INTEGER_ZERO;
        //后台菜单删除,遍历删除所有的角色菜单中间表
        pcUserCacheService.delMenuListByResource(menuId);
        pcMenuMapper.delRoleMenuByMenuId(menuId);
        int result = pcMenuMapper.deleteById(menuId);
        if (result > 0) {
            // 动态更新菜单
            DynamicSecurityMetadataSource.setConfigAttributeMap(dynamicSecurityService.loadDataSource());
        }
        return result;
    }

    @Override
    public List<String> getByUserId(Long menuId) {
        return pcMenuMapper.selectByUserId(menuId);
    }

    @Override
    public List<PcMenuTree> listAllMenu() {
        return pcMenuMapper.listAllMenu();
    }
}
