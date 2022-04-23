package com.ys.mail.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author DT
 * @version 1.0
 * @date 2021-11-24 13:36
 */
@ConfigurationProperties(prefix = "task.pool")
@Data
public class TaskThreadPoolConfig {
    /**
     * 核心线程数（默认线程数）
     */
    private int corePoolSize;
    /**
     * 最大线程数
     */
    private int maxPoolSize;
    /**
     * 允许线程空闲时间（单位：默认为秒）
     */
    private int keepAliveTime;
    /**
     * 缓冲队列数
     */
    private int queueCapacity;
    /**
     * 线程池名前缀
     */
    private String threadNamePrefix;

}
