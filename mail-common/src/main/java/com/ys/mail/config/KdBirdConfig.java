package com.ys.mail.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 用一句简单的话来描述下该类
 *
 * @author DT
 * @date 2022-06-07 17:32
 * @since 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "kd.bird")
public class KdBirdConfig {

    private String appId;
    private String appKey;
    private String reqUrl;
}
