package com.Lsrs.timedtasks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableDiscoveryClient
@EnableFeignClients //开启Feign远程调用功能
@EnableScheduling // 启用定时任务
@SpringBootApplication
public class TimedtasksMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(TimedtasksMainApplication.class, args);
    }
}
