package com.Lsrs.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.ComponentScan;

@EnableDiscoveryClient//开启服务发现功能
@SpringBootApplication
@FeignClient
@ComponentScan(basePackages = {"com.Lsrs.user", "com.example.redis"})
@MapperScan("com.Lsrs.user.Mapper")
public class UserMainApplication {
   public static void main(String[] args) {
       SpringApplication.run(UserMainApplication.class,args);
   }
}
