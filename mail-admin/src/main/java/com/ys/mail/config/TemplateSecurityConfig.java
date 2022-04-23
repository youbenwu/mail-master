package com.ys.mail.config;


import com.ys.mail.entity.PcMenu;
import com.ys.mail.security.component.DynamicSecurityService;
import com.ys.mail.security.config.SecurityConfig;
import com.ys.mail.service.PcMenuService;
import com.ys.mail.service.PcUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * template-security模块相关配置
 *
 * @author 070
 * @date 2021-5-17
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class TemplateSecurityConfig extends SecurityConfig {

    @Autowired
    private PcUserService userService;

    @Autowired
    private PcMenuService menuService;

    @Override
    @Bean
    public UserDetailsService userDetailsService() {
        //获取登录用户信息
        return username -> userService.loadUserByUsername(username);
    }

    @Bean
    public DynamicSecurityService dynamicSecurityService() {
        return () -> {
            Map<String, ConfigAttribute> map = new ConcurrentHashMap<>(16);
            List<PcMenu> resourceList = menuService.list();
            for (PcMenu menu : resourceList) {
                map.put(menu.getHttpMethod() + ":" + menu.getMenuUrl(), new org.springframework.security.access.SecurityConfig(menu.getMenuId() + ":" + menu.getMenuName()));
            }
            return map;
        };
    }
}
