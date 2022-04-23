package com.ys.mail.service.impl;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ys.mail.service.TaskService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;


/**
 * @author DT
 * @version 1.0
 * @date 2021-11-24 13:47
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class TaskServiceImpl implements TaskService {


    @Async
    @Override
    public void doTaskA() throws InterruptedException {
        System.out.println("TaskA thread name->" + Thread.currentThread().getName() + "start");
        Long startTime = System.currentTimeMillis();
        TimeUnit.SECONDS.sleep(60) ;
        Long endTime = System.currentTimeMillis();
        System.out.println("TaskA thread name->" + Thread.currentThread().getName() + "finish");
        System.out.println("TaskA 耗时：" + (endTime - startTime));
    }


    @Async
    @Override
    public void doTaskB() throws InterruptedException{
        System.out.println("TaskB thread name->" + Thread.currentThread().getName() + "start");
        Long startTime = System.currentTimeMillis();
        // 测试60s,检查是否异步
        TimeUnit.SECONDS.sleep(60);
        Long endTime = System.currentTimeMillis();
        System.out.println("TaskB thread name->" + Thread.currentThread().getName() + "finish");
        System.out.println("TaskB耗时：" + (endTime - startTime));
    }
}
