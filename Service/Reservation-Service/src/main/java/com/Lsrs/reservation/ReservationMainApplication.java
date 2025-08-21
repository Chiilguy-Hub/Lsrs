package com.Lsrs.reservation;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;


@EnableDiscoveryClient
@EnableFeignClients //开启Feign远程调用功能
@SpringBootApplication
@MapperScan("com.Lsrs.reservation.Mapper")
@ComponentScan(basePackages = {"com.Lsrs.reservation", "com.example.redis"})
public class ReservationMainApplication {
   public static void main(String[] args) {
       SpringApplication.run(ReservationMainApplication.class,args);
   }
}
