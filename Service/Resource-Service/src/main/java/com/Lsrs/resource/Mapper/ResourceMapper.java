package com.Lsrs.resource.Mapper;

import com.Lsrs.resource.pojo.room_seat;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.Dao.QrRequest;
import com.example.Dao.RoomCapacityDTO;
import com.example.pojo.SeatReservation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface ResourceMapper extends BaseMapper<room_seat> {
    @Select("SELECT seat_number FROM sys_room.room_seats WHERE zone_number = #{zone_number} and seat_number  like CONCAT('%', #{zone}, '%')and type=1")
    List<String> getAllRoom(int zone_number,String zone);

    @Select("select zone_number from sys_room.room_seats where seat_floor=#{floor} group by zone_number")
    List<Map<String, Integer>> getRoombyfloor(String floor);

    @Select("select zone_number , count(zone_number) as'count' from sys_room.room_seats group by zone_number")
    List<RoomCapacityDTO> getcount();
    @Select("select count(id) from sys_room.room_seats ")
    Integer getcount1();

    List<room_seat> getRoom(@Param("limit1") Integer limit1, @Param("zoneNumber")Integer zoomNumber);

    List<QrRequest> getqrseat(@Param("seatFloor") Integer seatFloor,@Param("zoneNumber") Integer zoneNumber,@Param("seatNumber") String seatNumber);
}
