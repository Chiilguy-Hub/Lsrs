package com.Lsrs.reservation.Mapper;

import com.example.Dao.scoreDao;
import com.example.pojo.SeatReservation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.Dao.RoomCapacityDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;


public interface ReservationMapper extends BaseMapper<SeatReservation> {

    Integer selecttime(@Param("endTime") LocalDateTime endTime, @Param("zoneNumber") Integer zoneNumber);


 @Select("update sys_reservation.seat_reservation set status=4 where status=3 and end_time<=now() ")
    void update_outtime();

 @Select("select *  from sys_reservation.seat_reservation where status=1 and start_time <=#{checkdate} ")
  List<SeatReservation> notice_checkin( LocalDateTime checkdate);

@Select("select * from sys_reservation.seat_reservation where status=4")
    List<SeatReservation> select_status4();

@Select("select * from sys_reservation.seat_reservation where start_time<=#{chekdate} and status=3")
    List<SeatReservation> notice_check(LocalDateTime checkdate);

@Select("update sys_reservation.seat_reservation set status=#{status} where user_id=#{userId} and status=#{oldstatus}  ")
void updatebyid(@Param("userId") String userId,@Param("status") int status,@Param("oldstatus") int oldstatus );

@Select("select * from sys_reservation.seat_reservation where user_id=#{userId} and status in(1,3)")
    SeatReservation selectBy_Id(String userId);

@Select("select * from sys_reservation.seat_reservation where user_id=#{userId} ORDER BY created_at DESC limit 1 ")
    SeatReservation getnewreservation(String userId);

@Select("select  zone_number, count(zone_number)as count from sys_reservation.seat_reservation where user_id=#{userId} group by zone_number order by count DESC LIMIT 3")
    List<RoomCapacityDTO> getoftenreservation(String userId);

    @Select("select * from sys_reservation.seat_reservation where user_id=#{userId} ORDER BY start_time DESC")
    List<SeatReservation> getallReservation(String userId);

@Select("select sum( TIMESTAMPDIFF(MINUTE , start_time, end_time))as'count'   from  sys_reservation.seat_reservation where status=4 and user_id=#{userId}")
    Integer getscore(String userId);

@Select("select user_id,sum( TIMESTAMPDIFF(MINUTE , start_time, end_time))as'count'   from  sys_reservation.seat_reservation where status=4 group by user_id order by  count desc limit 50;")
    List<scoreDao> getscore50();

    Integer getreseRvationCountbytime(@Param("startTime")LocalDateTime startTime,@Param("endTime") LocalDateTime endTime,@Param("status")Integer status,@Param("zoneNumber") Integer zoneNumber);


    Integer getreseRvationCountbycreattime(@Param("startTime")LocalDateTime startTime,@Param("endTime") LocalDateTime endTime);

    List<Integer> getroomrank(@Param("startTime")LocalDateTime startTime,@Param("endTime") LocalDateTime endTime,@Param("number")Integer number);

    List<SeatReservation> getReservation( @Param("limit1")Integer limit1, @Param("startTime")LocalDateTime startTime,@Param("endTime") LocalDateTime endTime, @Param("status")Integer tab);
}
