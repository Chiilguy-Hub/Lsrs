package com.Lsrs.reservation.feign;

import com.example.pojo.SeatReservation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@FeignClient("Notice-Service")
public interface NoticeFeignClient {
    @PostMapping(("/api/notice/checkin"))
    void reservationCheckin(@RequestBody List<SeatReservation> seatReservations);

    @PostMapping(("/api/notice/reservationnotice"))
    void reservationnotice(@RequestBody  SeatReservation seatReservations);

}
