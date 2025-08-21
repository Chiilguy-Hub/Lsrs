package com.Lsrs.login;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;


@EnableDiscoveryClient
@EnableFeignClients
@ComponentScan(basePackages = {"com.Lsrs.login", "com.example.redis"})
@SpringBootApplication

public class LoginMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(LoginMainApplication.class, args);
    }
}