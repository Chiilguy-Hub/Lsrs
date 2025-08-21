package com.Lsrs.check_in.feign;

import com.example.pojo.Result;
import com.example.pojo.SeatReservation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "Reservation-Service")
public interface ReservationFeignClient {
    @GetMapping("/api/reservation/updatecheck/{userId}")
    public void updatecheck(@PathVariable String userId);
    @GetMapping("/api/reservation/getReservationnew/{userId}")
    public SeatReservation getReservationnew(@PathVariable String userId);
}
