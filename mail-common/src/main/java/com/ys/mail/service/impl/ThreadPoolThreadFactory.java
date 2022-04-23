package com.ys.mail.service.impl;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author DT
 * @version 1.0
 * @date 2021-11-18 10:35
 */

public class ThreadPoolThreadFactory implements ThreadFactory {

    private AtomicInteger atomicInteger = new AtomicInteger();
    private String threadName;

    public ThreadPoolThreadFactory() {
        this(ThreadPoolThreadFactory.class.getSimpleName());
    }

    public ThreadPoolThreadFactory(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public Thread newThread(Runnable r) {
        int number = atomicInteger.getAndUpdate(p -> (p + 1) % Integer.MAX_VALUE);
        return new Thread(r, threadName + number);
    }

}
