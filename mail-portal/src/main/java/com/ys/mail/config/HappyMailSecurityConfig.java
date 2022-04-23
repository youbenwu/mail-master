package com.ys.mail.config;


import com.ys.mail.bo.UmsUserDetails;
import com.ys.mail.entity.UmsUser;
import com.ys.mail.security.component.DynamicSecurityService;
import com.ys.mail.security.config.SecurityConfig;
import com.ys.mail.service.UmsUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * mall-security模块相关配置
 *
 * @author 070
 * @date 2020/11/5
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class HappyMailSecurityConfig extends SecurityConfig {

    @Autowired
    private UmsUserService userService;

    /**
     * spring启动的时候注入bean,覆写security必须使用@Override注解
     *
     * @return
     */
    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return phone -> userService.loadBySecurity(phone);
    }

    @Bean
    public DynamicSecurityService dynamicSecurityService() {
        return () -> {
            Map<String, ConfigAttribute> map = new ConcurrentHashMap<>();
            // 特殊需求特殊处理，登录就能操作，*:表示支持method比对
            map.put("*:/**/**", new org.springframework.security.access.SecurityConfig("LOGIN"));
            return map;
        };
    }
}
