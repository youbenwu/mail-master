package com.ys.mail.util;

import cn.hutool.extra.spring.SpringUtil;
import com.ys.mail.config.ThreadPoolConfig;
import com.ys.mail.domain.ThreadPoolsInfo;
import com.ys.mail.service.impl.ThreadPoolThreadFactory;
import org.springframework.util.StringUtils;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author DT
 * @version 1.0
 * @date 2021-11-18 10:37
 */

public class ThreadPoolFactory {

    public static final String DEFAULT_POOL = "DEFAULT_POOL";

    private static final Map<String, ThreadPoolExecutor> poolMap = new ConcurrentHashMap<>();

    private static ThreadPoolConfig threadPoolConfig = null;

    public static ThreadPoolExecutor getPool() {
        return getPool(DEFAULT_POOL);
    }

    /**
     * @param poolName
     * @return
     */
    public static ThreadPoolExecutor getPool(String poolName) {
        if (poolMap.containsKey(poolName)) {
            return poolMap.get(poolName);
        } else {
            synchronized (poolMap) {
                if (!poolMap.containsKey(poolName)) {
                    if (threadPoolConfig == null) {
                        threadPoolConfig = SpringUtil.getBean(ThreadPoolConfig.class);
                    }
                    poolMap.put(poolName,
                            new ThreadPoolExecutor(threadPoolConfig.getCorePoolSize(),
                                    threadPoolConfig.getMaximumPoolSize(), threadPoolConfig.getKeepAliveTime(),
                                    TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(threadPoolConfig.getQueueSize()),
                                    new ThreadPoolThreadFactory(DEFAULT_POOL), new ThreadPoolExecutor.AbortPolicy()));
                }
            }
            return poolMap.get(poolName);
        }
    }

    /**
     * @param poolName
     * @param corePoolSize
     * @param maximumPoolSize
     * @param keepAliveTime
     * @param queueSize
     * @param handler
     * @return
     */
    public static ThreadPoolExecutor getPool(String poolName, int corePoolSize, int maximumPoolSize, int keepAliveTime,
                                             int queueSize, RejectedExecutionHandler handler) {
        if (poolMap.containsKey(poolName)) {
            return poolMap.get(poolName);
        } else {
            synchronized (poolMap) {
                if (!poolMap.containsKey(poolName)) {
                    if (threadPoolConfig == null) {
                        threadPoolConfig = SpringUtil.getBean(ThreadPoolConfig.class);
                    }
                    poolMap.put(poolName,
                            new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MILLISECONDS,
                                    new ArrayBlockingQueue<>(queueSize), new ThreadPoolThreadFactory(DEFAULT_POOL),
                                    handler));
                }
            }
            return poolMap.get(poolName);
        }
    }

    /**
     * @return
     */
    public static ThreadPoolsInfo getInfo() {
        ThreadPoolsInfo info = new ThreadPoolsInfo();
        Iterator<Map.Entry<String, ThreadPoolExecutor>> iterator = poolMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, ThreadPoolExecutor> next = iterator.next();
            String key = next.getKey();
            ThreadPoolExecutor value = next.getValue();
            ThreadPoolsInfo.ThreadPoolInfo poolInfo = new ThreadPoolsInfo.ThreadPoolInfo(value);
            info.getInfos().put(key, poolInfo);
            info.appendActiveCount(poolInfo.getActiveCount());
            info.appendWaitingSize(poolInfo.getWaitingSize());
        }
        return info;
    }

    public static void shutdown(String poolName) {
        if (StringUtils.isEmpty(poolName)) {
            return;
        }
        ThreadPoolExecutor threadPoolExecutor = poolMap.get(poolName);
        if (threadPoolExecutor == null) {
            return;
        }
        synchronized (poolMap) {
            if (poolMap.containsKey(poolName)) {
                poolMap.remove(poolName);
                if (!threadPoolExecutor.isShutdown()) {
                    threadPoolExecutor.shutdownNow();
                }
                boolean isShutdown = false;
                try {
                    isShutdown = threadPoolExecutor.awaitTermination(1000L, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!isShutdown) {
                    System.out.println("Invoke thread pool shutdown ,but raised exception .");
                }
            }
        }
    }

}
