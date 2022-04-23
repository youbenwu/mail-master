package com.ys.mail.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author DT
 * @version 1.0
 * @date 2021-11-24 13:43
 */
@Configuration
public class MyThreadPoolTaskExecutor {

    @Autowired
    private TaskThreadPoolConfig config;

    /**
     * 默认异步线程池
     *
     * @return
     */
    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        pool.setThreadNamePrefix(config.getThreadNamePrefix());
        pool.setCorePoolSize(config.getCorePoolSize());
        pool.setMaxPoolSize(config.getMaxPoolSize());
        pool.setKeepAliveSeconds(config.getKeepAliveTime());
        pool.setQueueCapacity(config.getQueueCapacity());
        // 直接在execute方法的调用线程中运行
        pool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待所有任务结束后再关闭线程池
        pool.setWaitForTasksToCompleteOnShutdown(true);
        // 初始化
        pool.initialize();
        return pool;
    }
}
