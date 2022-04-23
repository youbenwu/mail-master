package com.ys.mail.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Desc
 * @Author CRH
 * @Create 2022-01-05 13:47
 */
@Data
@Component
@ConfigurationProperties(prefix = "cos")
public class CosConfig {

    private String secretId;
    private String secretKey;
    private String bucket;
    private String region;
    private String uploadFolder;
    private String cdnDomain;
    private Integer durationSeconds;
    private Integer threadPool;
    private String[] allowPrefixes;
    private String[] allowActions;

}
