package com.ys.mail.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @Desc springboot 异步配置
 * @Author CRH
 * @Create 2022-01-07 18:34
 */
@Configuration
public class AsyncConfig {

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);// CPU线程数
        executor.setMaxPoolSize(100);// 最大线程数
        executor.setQueueCapacity(10);// 队列数
        executor.initialize();
        return executor;
    }
}
