package com.Lsrs.reservation;

import com.Lsrs.reservation.Mapper.ReservationMapper;
import com.Lsrs.reservation.feign.NoticeFeignClient;
import com.Lsrs.reservation.service.Reservationservice;
import com.example.pojo.SeatReservation;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@MapperScan("com.example.demo.mapper")
@ComponentScan(basePackages = {"com.Lsrs.reservation", "com.example.redis"})
@SpringBootTest
public class Text {
    @Autowired
    ReservationMapper reservationMapper;
@Autowired
    Reservationservice reservationservice;
    @Autowired
    NoticeFeignClient noticeFeignClient;
    @Test
    public void test() {
        LocalDate date = LocalDate.now();
        LocalDateTime startTime=date.atTime(7,0); //起始时间
        LocalDateTime endTime=date.atTime(22,0);//结束时间
        LocalDateTime day7startTime = startTime.minusDays(7);

        System.out.println(day7startTime);

    }
    public static List<LocalDateTime> generateTimeSlots(LocalDate date) {
        List<LocalDateTime> timeSlots = new ArrayList<>();

        // 定义起始和结束时间（结合传入的日期）
        LocalDateTime startTime = date.atTime(8, 0);   // 8:00
        LocalDateTime endTime = date.atTime(21, 0);    // 21:00
        LocalDateTime currentTime = startTime;
        // 循环生成时间点，直到超过结束时间
        while (!currentTime.isAfter(endTime)) {
            timeSlots.add(currentTime);
            currentTime = currentTime.plusMinutes(15); // 每次增加15分钟
        }

        return timeSlots;
    }
    @Test
    public void test2() {
      SeatReservation seatReservation = new SeatReservation();
      seatReservation.setUserId("210501050011");
      seatReservation.setSeatNumber("A01");
      seatReservation.setStartTime(LocalDateTime.now());
      seatReservation.setEndTime(LocalDateTime.now().plusHours(2));

      seatReservation.setStatus(1);
      seatReservation.setCreatedAt(LocalDateTime.now());
      seatReservation.setUpdatedAt(LocalDateTime.now());
      List<SeatReservation> list=new ArrayList<>();
      list.add(seatReservation);
    noticeFeignClient.reservationCheckin(list);


    }
}
