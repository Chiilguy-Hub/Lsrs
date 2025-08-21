package com.Lsrs.timedtasks.feign;

import com.example.pojo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;



@FeignClient(value = "ResourceFeign-Service")
public interface ResourceFeign {
    @PostMapping("/loadredis")
    Result loadRedis();
}
