package com.Lsrs.reservation.service;

import com.Lsrs.reservation.pojo.Echartsdata;
import com.Lsrs.reservation.pojo.panelsDao;
import com.example.Dao.scoreDao;
import com.example.pojo.PageResult;
import com.example.pojo.SeatReservation;
import com.example.Dao.RoomCapacityDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface Reservationservice {
     String reservation(SeatReservation appointment);//将预约信息写入数据库

     //根据当前 zone_number 获取到已经使用过的seat_number
     List getseats(int zone_muber, LocalDateTime start, LocalDateTime end,String zone);

     SeatReservation  getreservation(String userId);

     //完成每个时间段的容量初始化在redis
     void setInitialize(List<RoomCapacityDTO> count);



     void  update_outtime();

     List<SeatReservation> notice_checkin(LocalDateTime checkdate);

     List<SeatReservation> select_status4();

     List<SeatReservation> notice_check(LocalDateTime checkdate);

     SeatReservation getnewreservation(String userId);

     List<RoomCapacityDTO> getoftenreservation(String userId);

     List<SeatReservation> getallReservation(String userId);

     Integer getscore(String userId);

     List<scoreDao> getscore50();



     void cancel(String userId, int i,int oldStatus);



    List<panelsDao> initPanels();

     Map<String, List<String>> getechartsdata(String type);

     Echartsdata getechartsdata1(Integer count);

    PageResult<SeatReservation> getReservationList(Integer page, Integer limit, LocalDateTime startTime, LocalDateTime endTime, Integer tab);
}
