package com.Lsrs.resource.service.impl;


import com.Lsrs.resource.Mapper.ResourceMapper;
import com.Lsrs.resource.pojo.room_seat;
import com.Lsrs.resource.service.ResourceService;

import com.example.Dao.QrRequest;
import com.example.Dao.RoomCapacityDTO;
import com.example.pojo.PageResult;
import com.example.pojo.SeatReservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ResourceServiceImpl  implements ResourceService {
@Autowired
private ResourceMapper resourceMapper;




    @Override
    public List<String> getAllRoom(int zone_muber,String zone) {
        List allRoom = resourceMapper.getAllRoom(zone_muber,zone);
        if(allRoom.size()==0){
            List<String> list = new ArrayList<>();
            return list;
        }
        return allRoom;
    }

    @Override
    public List<RoomCapacityDTO> getcount() {
        return resourceMapper.getcount();
    }

    @Override
    public List<Map<String, Integer>> roombyfloor(String floor) {
        return resourceMapper.getRoombyfloor(floor);
    }

    @Override
    public PageResult<room_seat> getROOM(Integer page, Integer limit, Integer zoomNumber) {
        PageResult<room_seat> room_seatPageResult = new PageResult<>();
        Integer count=resourceMapper.getcount1();//获取对应的数量
        Integer limit1 = (page - 1) * limit;
        List<room_seat> list=resourceMapper.getRoom(limit1,zoomNumber);
        room_seatPageResult.setList(list);
        room_seatPageResult.setTotal(count);
        return room_seatPageResult;
    }

    @Override
    public void insert(room_seat room) {
        resourceMapper.insert(room);
    }

    @Override
    public void update(room_seat room) {
        resourceMapper.updateById(room);
    }

    @Override
    public void delete(Integer id) {
        resourceMapper.deleteById(id);
    }

    @Override
    public List<QrRequest> getqrseat(QrRequest qrRequest) {
        Integer seatFloor = qrRequest.getSeatFloor();
        Integer zoneNumber = qrRequest.getZoneNumber();
        String seatNumber = qrRequest.getSeatNumber();
        List<QrRequest> getqrseat = resourceMapper.getqrseat(seatFloor,zoneNumber,seatNumber);
        return getqrseat;
    }
}
