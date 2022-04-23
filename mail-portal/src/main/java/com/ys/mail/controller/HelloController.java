package com.ys.mail.controller;

import com.ys.mail.service.TaskService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author DT
 * @version 1.0
 * @date 2021-11-24 13:45
 */
@RestController
@Validated
@RequestMapping(value = "/hello")
@Api(tags = "线程池测试管理")
public class HelloController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/async")
    public String testAsync() throws Exception {
        System.out.println("主线程 name -->" + Thread.currentThread().getName() + "start");
        Long startTime = System.currentTimeMillis();
        taskService.doTaskA();
        taskService.doTaskB();
        Long endTime = System.currentTimeMillis();
        System.out.println("主线程 name -->" + Thread.currentThread().getName() + "finish");
        System.out.println("主线程" + Thread.currentThread().getName() + "耗时：" + (endTime - startTime));
        return "Hello World";
    }
}
