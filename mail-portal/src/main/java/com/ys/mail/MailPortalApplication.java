package com.ys.mail;

import com.ys.mail.config.TaskThreadPoolConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author 070
 * @date 2021/1/5 10:16
 */
@EnableAsync
@SpringBootApplication
@EnableConfigurationProperties({TaskThreadPoolConfig.class})
public class MailPortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(MailPortalApplication.class, args);
    }

}
