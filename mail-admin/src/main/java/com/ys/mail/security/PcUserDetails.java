package com.ys.mail.security;


import com.ys.mail.entity.PcMenu;
import com.ys.mail.entity.PcUser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @version 1.0
 * @author： DT
 * @date： 2021-09-17 11:34
 */
@Getter
@Setter
public class PcUserDetails implements UserDetails{

    /**
     * 用户对象
     */
    private PcUser user;

    /**
     * 权限集合
     */
    private List<PcMenu> resourceList;

    /**
     * 有参构造器
     * @param user 用户对象
     * @param resourceList 权限集合
     */
    public PcUserDetails(PcUser user, List<PcMenu> resourceList) {
        this.user = user;
        this.resourceList = resourceList;
    }

    /**
     * 返回的权限集合
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //返回当前用户的角色
        return resourceList.stream().
                map(menu -> new SimpleGrantedAuthority(menu.getMenuId()+ ":"+menu.getMenuName())).collect(Collectors.toList());
    }

    /**
     * 验证密码
     * @return
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * 验证用户名
     * @return
     */
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    /**
     * 判断账号是否过期-DT
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    /**
     *判断账号是否锁定-DT
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    /**
     * 判断用户凭证是否过期-DT
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    /**
     * 用户是否可用-DT
     * @return
     */
    @Override
    public boolean isEnabled() {
        return user.getUserStatus();
    }
}
