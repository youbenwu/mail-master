package com.ys.mail.security.component;

import org.springframework.security.access.ConfigAttribute;

import java.util.Map;

/**
 * 动态权限相关业务类
 *
 * @author macro
 * @date 2020/2/7
 */
public interface DynamicSecurityService {
    /**
     * 加载资源ANT通配符和资源对应MAP
     * @return 返回值
     */
    Map<String, ConfigAttribute> loadDataSource();
}
