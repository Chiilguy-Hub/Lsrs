package com.Lsrs.notice.service;

import com.Lsrs.notice.pojo.SeatReservation;
import com.Lsrs.notice.pojo.UserNotification;

import java.util.List;

public interface Noticeservice {
    void sendnotice(List<SeatReservation> seatReservations);

    List<UserNotification> getnoticebyId(String userId);

    void sendreservationnotice(SeatReservation seatReservations);
}
