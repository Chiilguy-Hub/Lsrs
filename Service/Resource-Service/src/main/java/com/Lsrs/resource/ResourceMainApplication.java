package com.Lsrs.resource;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.Lsrs.resource.Mapper")
@ComponentScan(basePackages = {"com.Lsrs.resource", "com.example.redis"})
public class ResourceMainApplication {
   public static void main(String[] args) {
       SpringApplication.run(ResourceMainApplication.class,args);
   }
}
