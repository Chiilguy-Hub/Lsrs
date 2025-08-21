package com.Lsrs.feedback;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
@MapperScan("com.Lsrs.feedback.Mapper")
public class FeedbackMainApplication {
    public static void main(String[] args) {
            SpringApplication.run(FeedbackMainApplication.class, args);
    }
}
