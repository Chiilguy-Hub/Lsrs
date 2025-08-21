package com.Lsrs.timedtasks.feign;

import com.example.pojo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "Reservation-Service")
public interface ReservationFeign {
   @GetMapping("api/reservation/updatestate_1")
   void updatestate_1();
   @GetMapping("api/reservation/updatestate_2")
   void updatestate_2();
   @GetMapping("api/reservation/updatestate_3")
   void updatestate_3();
   @PostMapping("/api/reservation/loadredis")
   Result loadRedis();
}
