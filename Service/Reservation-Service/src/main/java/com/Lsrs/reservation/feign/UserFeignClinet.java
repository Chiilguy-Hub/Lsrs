package com.Lsrs.reservation.feign;

import com.example.Dao.TimeDao;
import com.example.Dao.scoreDao;
import com.example.pojo.Result;
import com.example.pojo.SeatReservation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@FeignClient("User-Service")
public interface UserFeignClinet {
    @PostMapping("/api/user/getuser")
  List<scoreDao> reservationCheckin(@RequestBody List<scoreDao> users);

    @PostMapping("/api/user/getcount")
    Integer getcount(@RequestBody TimeDao timeDao);

    @GetMapping("/api/user/blacklist/{user_id}")
    boolean getblacklist(@PathVariable("user_id") String userId);
  @PostMapping("/api/user/updata_shuju")
   Result updataShuju(@RequestBody List<SeatReservation> seatReservations);
}
