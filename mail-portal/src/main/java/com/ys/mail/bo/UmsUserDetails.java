package com.ys.mail.bo;


/*import com.ys.mail.entity.UserAccount;*/
import com.ys.mail.entity.UmsUser;
import com.ys.mail.exception.ApiException;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * 会员详情封装类
 * 2021-5-5 implements UserDetails
 * @author ghdhj
 */
public class UmsUserDetails implements UserDetails{

    /**
     * 注入用户对象
     */
    private UmsUser user;

    public UmsUser getUmsUser(){
        return user;
    }

    /**
     * 注入有参构造器
     * @param user
     */
    public UmsUserDetails(UmsUser user){
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 返回当前用户的权限,LOGIN代表登录就可以操作
        return Collections.singletonList(new SimpleGrantedAuthority("LOGIN"));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getPhone();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return user.getUserStatus();
    }
}
