package com.ys.mail;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author 070
 * @date 2021/1/14 23:32
 */
@EnableAsync
@EnableScheduling
@SpringBootApplication
@EnableConfigurationProperties
public class MailAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(MailAdminApplication.class, args);
    }

}
