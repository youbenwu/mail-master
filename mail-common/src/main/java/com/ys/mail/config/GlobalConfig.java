package com.ys.mail.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 全局配置类
 *
 * @author CRH
 * @date 2022-04-19 17:00
 * @since 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "global")
public class GlobalConfig {

    /**
     * 是否开发模式，true->开发
     */
    private Boolean devMode;

    /**
     * 项目中文名称，可统一用于其他需要的地方显示，如swagger等
     */
    private String projectName;

    /**
     * APP客户端名称
     */
    private String appZeroName;

    private String appOneName;

    /**
     * 根据平台类型返回APP客户端名称
     *
     * @param type 平台类型
     * @return 结果
     */
    public String appName(Integer type) {
        switch (type) {
            case 0:
                return this.appZeroName;
            case 1:
            default:
                return this.appOneName;
        }
    }

}
