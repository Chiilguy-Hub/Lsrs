package com.Lsrs.check_in;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@ComponentScan(basePackages = {"com.Lsrs.check_in", "com.example.redis"})
public class Check_inMainApplication {
   public static void main(String[] args) {
       SpringApplication.run(Check_inMainApplication.class,args);
   }
}
