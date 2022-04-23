package com.ys.mail.runner;

import com.ys.mail.service.SysSettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @Desc 程序初始化的执行一些任务，每次重启将会执行run方法
 * @Author CRH
 * @Create 2022-02-19 12:27
 */
@Component
public class InitApplicationRunner implements ApplicationRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(InitApplicationRunner.class);

    @Autowired
    private SysSettingService sysSettingService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        LOGGER.info("InitApplicationRunner 开始加载 ···");

        // 重新加载系统设置
        sysSettingService.loadAll();
        // 其他操作等

        LOGGER.info("InitApplicationRunner 加载完成 ···");
    }
}
