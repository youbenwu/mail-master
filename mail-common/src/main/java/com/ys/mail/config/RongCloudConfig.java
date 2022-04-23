package com.ys.mail.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Desc
 * @Author CRH
 * @Create 2022-01-13 14:37
 */
@Data
@Component
@ConfigurationProperties(prefix = "rongcloud")
public class RongCloudConfig {

    private String appKey;
    private String appSecret;
    private String apiUrl;
    private String defaultPortraitUrl;

}
