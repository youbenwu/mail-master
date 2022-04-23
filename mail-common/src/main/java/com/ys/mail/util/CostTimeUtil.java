package com.ys.mail.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Desc 通用耗时统计类
 * @Author CRH
 * @Create 2021-12-31 17:54
 */
public class CostTimeUtil implements AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(CostTimeUtil.class);

    private final long start;
    private final String taskName;

    public CostTimeUtil(String taskName) {
        this.start = System.currentTimeMillis();
        this.taskName = taskName;
        LOGGER.info("【{}】 --> 任务开始执行", taskName);
    }

    @Override
    public void close() {
        LOGGER.info("【{}】 --> 任务执行耗时：{} ms", taskName, (System.currentTimeMillis() - start));
    }
}
