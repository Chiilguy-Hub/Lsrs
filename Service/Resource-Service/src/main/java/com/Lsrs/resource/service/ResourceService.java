package com.Lsrs.resource.service;


import com.Lsrs.resource.pojo.room_seat;
import com.example.Dao.QrRequest;
import com.example.Dao.RoomCapacityDTO;
import com.example.pojo.PageResult;
import com.example.pojo.SeatReservation;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ResourceService {

    List<String> getAllRoom(int zone_muber,String zone);

    List<RoomCapacityDTO> getcount();

    List<Map<String, Integer>> roombyfloor(String floor);

    PageResult<room_seat> getROOM(Integer page, Integer limit, Integer zoomNumber);

    void insert(room_seat room);

    void update(room_seat room);

    void delete(Integer id);

    List<QrRequest> getqrseat( QrRequest qrRequest);
}
