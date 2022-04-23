package com.ys.mail.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.mail.entity.PcMenu;
import com.ys.mail.entity.PcUser;
import com.ys.mail.exception.ApiException;
import com.ys.mail.mapper.PcUserMapper;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.param.PcUserParam;
import com.ys.mail.model.admin.param.PcUserRegisterParam;
import com.ys.mail.model.admin.tree.PcMenuTree;
import com.ys.mail.model.admin.vo.UserImInfoVO;
import com.ys.mail.model.admin.vo.UserLoginVO;
import com.ys.mail.model.admin.vo.UserOwnRoleVO;
import com.ys.mail.security.PcUserDetails;
import com.ys.mail.security.util.JwtTokenUtil;
import com.ys.mail.service.*;
import com.ys.mail.util.*;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * <p>
 * 后台用户表 服务实现类
 * </p>
 *
 * @author 070
 * @since 2021-10-20
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class PcUserServiceImpl extends ServiceImpl<PcUserMapper, PcUser> implements PcUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PcUserServiceImpl.class);

    @Autowired
    private PcUserMapper pcUserMapper;
    @Autowired
    private PcUserCacheService pcUserCacheService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PcMenuService pcMenuService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Autowired
    private PcRoleService roleService;
    @Autowired
    private RongCloudService rongCloudService;
    @Autowired
    private CosService cosService;

    @Override
    public CommonResult<Boolean> register(PcUserRegisterParam param) {
        if (BlankUtil.isNotEmpty(param.getPcUserId())) return CommonResult.failed("非法请求", Boolean.FALSE);
        PcUserParam pcUserParam = new PcUserParam();
        BeanUtils.copyProperties(param, pcUserParam);
        return this.createAndUpdateUser(pcUserParam);
    }

    @Override
    public CommonResult<Boolean> createAndUpdateUser(PcUserParam param) {
        // 新增或修改判断
        String userId = param.getPcUserId();
        PcUser pcUser = new PcUser();

        if (BlankUtil.isEmpty(userId)) { // 注册新增
            // 判断用户名是否已经存在
            PcUser tempUser = this.getUserByUsername(param.getUsername());
            if (BlankUtil.isNotEmpty(tempUser)) return CommonResult.failed("用户名已存在", Boolean.FALSE);
            LOGGER.info("【添加后台用户】：{}", param.getUsername());
            pcUser.setPcUserId(IdWorker.generateId());
            pcUser.setUsername(param.getUsername());
            // 加密
            pcUser.setPassword(passwordEncoder.encode(param.getPassword()));
            // 为空则随机生成
            pcUser.setNickname(BlankUtil.isNotEmpty(param.getNickname()) ? param.getNickname() : NickNameUtil.getRandom(3));
            // 为空则使用数据默认的
            pcUser.setHeadPortrait(BlankUtil.isNotEmpty(param.getHeadPortrait()) ? param.getHeadPortrait() : null);
        } else { // 修改
            pcUser = this.getById(userId);// 判断ID是否存在
            if (BlankUtil.isEmpty(pcUser)) return CommonResult.failed("用户ID不存在", Boolean.FALSE);
            LOGGER.info("【修改后台用户】：{}", pcUser.getUsername());
            if (BlankUtil.isNotEmpty(param.getPassword()))
                pcUser.setPassword(passwordEncoder.encode(param.getPassword()));
            if (BlankUtil.isNotEmpty(param.getNickname())) pcUser.setNickname(param.getNickname());
            if (BlankUtil.isNotEmpty(param.getHeadPortrait())) pcUser.setHeadPortrait(param.getHeadPortrait());
            pcUserCacheService.delUser(Long.valueOf(userId));
        }
        pcUser.setAllowDelete(Boolean.TRUE); // 禁止前台修改
        return saveOrUpdate(pcUser) ? CommonResult.success(Boolean.TRUE) : CommonResult.failed(Boolean.FALSE);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        PcUser user = getUserByUsername(username);
        Optional.ofNullable(user).orElseThrow(() -> new ApiException("用户名错误"));
        pcUserCacheService.setUser(user);
        List<PcMenu> menus = getMenuList(user.getPcUserId());
        return new PcUserDetails(user, menus);
    }

    @Override
    public UserLoginVO userLogin(String username, String password) {
        UserLoginVO vo = new UserLoginVO();
        UserDetails userDetails = loadUserByUsername(username);
        boolean matches = passwordEncoder.matches(password, userDetails.getPassword());
        if (!matches) {
            throw new ApiException("密码错误");
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        PcUserDetails pcUserDetails = (PcUserDetails) userDetails;
        PcUser pcUser = pcUserDetails.getUser();
        List<PcMenuTree> list = pcMenuService.listByUserId(username, pcUser.getPcUserId());
        list = TreeUtil.toTree(list, "menuId", "parentId", "children", PcMenuTree.class);
        String authToken = jwtTokenUtil.generateToken(userDetails);
        vo.setToken(tokenHead + authToken);
        vo.setMenuTrees(list);
        this.setExtendInfo(vo, pcUserDetails);
        return vo;
    }

    /**
     * 登录时：封装一些扩展信息
     *
     * @param vo vo
     */
    private void setExtendInfo(UserLoginVO vo, PcUserDetails pcUserDetails) {
        PcUser pcUser = PcUserUtil.getCurrentUser();
        // 添加COS路径
        vo.setCosFilePath(cosService.getOssPath());
        // 登录成功：获取融云token(只有包含权限才添加)
        boolean match = pcUserDetails.getResourceList().stream()
                                     .anyMatch(menu -> "/pc/im/getAppKey".equals(menu.getMenuUrl()));
        if (match) {
            String rongCloudAppKey = rongCloudService.getAppKey();
            String rongCloudToken = rongCloudService.register(pcUser.getPcUserId(), pcUser.getUsername(), pcUser.getHeadPortrait(), "0");
            vo.setRongCloudToken(rongCloudToken);
            vo.setRongCloudAppKey(rongCloudAppKey);
        }

        // 返回个人头像、昵称等信息
        vo.setUserInfo(UserImInfoVO.builder()
                                   .userId(String.valueOf(pcUser.getPcUserId()))
                                   .nickname(pcUser.getNickname())
                                   .headPortrait(pcUser.getHeadPortrait()).build());
    }

    @Override
    public int grantRole(Long pcUserId, List<Long> roleIds) {
        //先删除用户角色中间表的所有用户角色,能后再删除缓存中信息,最后插入
        pcUserMapper.delUserRoleByUserId(pcUserId);
        pcUserCacheService.delMenuList(pcUserId);
        Optional.ofNullable(roleIds).orElseThrow(() -> new ApiException("角色集合不能传null!!!"));
        List<Map<String, Long>> mapList = new ArrayList<>();
        List<Long> longs = IdWorker.generateIds(roleIds.size());
        for (int i = NumberUtils.INTEGER_ZERO; i < roleIds.size(); i++) {
            Map<String, Long> map = new HashMap<>(16);
            map.put("roleId", roleIds.get(i));
            map.put("userRoleId", longs.get(i));
            mapList.add(map);
        }
        return pcUserMapper.insertUserRole(pcUserId, mapList);
    }

    @Override
    public List<String> getAllUserIdByRoleId(Long roleId) {
        return pcUserMapper.selectAllUserIdByRoleId(roleId);
    }

    @Override
    public int delPcUser(Long pcUserId) {
        pcUserCacheService.delUser(pcUserId);
        pcUserCacheService.delMenuList(pcUserId);
        pcUserMapper.delUserRoleByUserId(pcUserId);
        return pcUserMapper.deleteById(pcUserId);
    }

    @Override
    public boolean logout(HttpServletRequest request) {
        // TODO 查看是否是用户退出和外部链接,设置token的过期时间
        String authorization = request.getHeader("Authorization");
        String authToken = authorization.substring(this.tokenHead.length());
        return pcUserCacheService.delToken(authToken);
    }

    @Override
    public UserOwnRoleVO getUserOwnRole(Long pcUserId) {
        return UserOwnRoleVO.builder().arr(pcUserMapper.getUserOwnRole(pcUserId)).roles(roleService.list()).build();
    }

    private List<PcMenu> getMenuList(Long pcUserId) {
        List<PcMenu> menus = pcUserCacheService.getResourceList(pcUserId);
        if (CollUtil.isNotEmpty(menus)) {
            return menus;
        }
        menus = pcMenuService.getMenuList(pcUserId);
        if (CollUtil.isNotEmpty(menus)) {
            pcUserCacheService.setMenuList(pcUserId, menus);
        }
        return menus;
    }

    /**
     * 从缓存中查询出用户是否存在，如果没有则到数据库中查询
     *
     * @param username 用户登录账号
     * @return 用户对象，如果存在则返回对象，不存在则为空
     */
    @Override
    public PcUser getUserByUsername(String username) {
        PcUser user = pcUserCacheService.getUser(username);
        if (BlankUtil.isEmpty(user)) {
            user = pcUserMapper.selectOne(new QueryWrapper<PcUser>().eq("username", username));
        }
        return user;
    }
}
