package com.ys.mail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.mail.entity.PcUser;
import com.ys.mail.model.CommonResult;
import com.ys.mail.model.admin.param.PcUserParam;
import com.ys.mail.model.admin.param.PcUserRegisterParam;
import com.ys.mail.model.admin.vo.UserLoginVO;
import com.ys.mail.model.admin.vo.UserOwnRoleVO;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 后台用户表 服务类
 * </p>
 *
 * @author 070
 * @since 2021-10-20
 */
public interface PcUserService extends IService<PcUser> {

    /**
     * 注册接口
     *
     * @param param 参数
     * @return true -> 注册成功,false -> 注册失败
     */
    CommonResult<Boolean> register(PcUserRegisterParam param);

    /**
     * 后台用户修改（注册也统一调用该方法）
     *
     * @param param 参数
     * @return true -> 新增成功,false -> 新增失败
     */
    CommonResult<Boolean> createAndUpdateUser(PcUserParam param);

    /**
     * 项目启动时获取当前用户的登录信息
     *
     * @param username
     * @return
     */
    UserDetails loadUserByUsername(String username);

    /**
     * 后台用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 返回实体
     */
    UserLoginVO userLogin(String username, String password);

    /**
     * 为用户授予角色
     *
     * @param pcUserId 用户id
     * @param roleIds  角色id集合
     * @return 返回值
     */
    int grantRole(Long pcUserId, List<Long> roleIds);

    /**
     * 根据角色id查询用户id集合
     *
     * @param roleId 角色id
     * @return 返回集合
     */
    List<String> getAllUserIdByRoleId(Long roleId);

    /**
     * 后台用户删除
     *
     * @param pcUserId 用户id
     * @return 返回值
     */
    int delPcUser(Long pcUserId);

    /**
     * 退出登录
     *
     * @param request 客户端请求
     * @return 返回值
     */
    boolean logout(HttpServletRequest request);

    /**
     * 获取角色拥有的菜单
     *
     * @param pcUserId 用户id
     * @return 返回值
     */
    UserOwnRoleVO getUserOwnRole(Long pcUserId);

    /**
     * 从缓存中查询出用户是否存在，如果没有则到数据库中查询
     *
     * @param username 用户登录账号
     * @return 用户对象
     */
    PcUser getUserByUsername(String username);

}
