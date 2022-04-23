package com.ys.mail.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author DT
 * @version 1.0
 * @date 2021-11-18 11:01
 */
@Data
@Component
public class ThreadPoolConfig {

    /**
     *
     * <pre>
     * corePoolSize : 核心线程数线程数定义了最小可以同时运行的线程数量。
     * maximumPoolSize :当队列中存放的任务达到队列容量的时候，当前可以同时运行的线程数量变为最大线程数。
     * keepAliveTime,unit: 存活时间
     * workQueue:当新任务来的时候会先判断当前运行的线程数量是否达到核心线程数，如果达到的话，新任务就会被存放在队列中。
     * handler: workQueue饱和策略.
     * </pre>
     *
     * <pre>
     * 饱和策略（如果线程数量达到最大线程数量并且队列也已经被放满）：
     * ThreadPoolExecutor.AbortPolicy：抛出 RejectedExecutionException来拒绝新任务的处理。
     * ThreadPoolExecutor.CallerRunsPolicy：调用当前线程运行任务。如果当前程序已关闭，则会丢弃该任务。
     * ThreadPoolExecutor.DiscardPolicy： 不处理新任务，直接丢弃掉。
     * ThreadPoolExecutor.DiscardOldestPolicy： 丢弃最早的未处理的任务请求。
     * </pre>
     */

    /**
     * 核心线程数
     */
    @Value("${config.corePoolSize}")
    private  int corePoolSize ;

    /**
     * 同时运行的最大线程数
     */
    @Value("${config.maximumPoolSize}")
    private  int maximumPoolSize ;

    /**
     * 存活时间
     */
    @Value("${config.keepAliveTime}")
    private  int keepAliveTime ;

    /**
     * 队列长度
     */
    @Value("${config.queueSize}")
    private  int queueSize ;
}
