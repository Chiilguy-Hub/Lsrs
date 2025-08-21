package com.Lsrs.notice.controller;

import com.Lsrs.notice.pojo.SeatReservation;
import com.Lsrs.notice.pojo.UserNotification;
import com.Lsrs.notice.service.Noticeservice;
import com.example.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notice")


public class NoticeController {
    @Autowired
    private Noticeservice noticeservice;

    @PostMapping("/checkin")
    void reservationCheckin(@RequestBody List<SeatReservation> seatReservations){
        System.out.println(seatReservations);
             noticeservice.sendnotice(seatReservations);
    }
    @GetMapping("/getnoticebyId/{userId}")
    Result getnoticebyId(@PathVariable String userId){
        List<UserNotification> userNotifications = noticeservice.getnoticebyId(userId);
                return Result.success(userNotifications);
    }
    @PostMapping(("/reservationnotice"))
    void reservationnotice(@RequestBody  SeatReservation seatReservations){
        noticeservice.sendreservationnotice(seatReservations);

    }
}
