package com.Lsrs.reservation.feign;

import com.example.pojo.SeatReservation;
import com.example.Dao.RoomCapacityDTO;
import com.example.pojo.room;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@FeignClient(value = "Resource-Service")//服务器的名称           //feign 客户端
public interface ResourceFeignClient { //业务间api调用
    //1、标注在Controller上，是接受请求。
    //2.标注在Feign上，是发送请求。
 @GetMapping("/api/resource/{id}")
    room reservation(@PathVariable("id") int id);

    @PostMapping("/api/resource/getall")
 List<String> getAllRoom(@RequestParam("zone_number") int zone_number ,@RequestParam("zone") String zone);


    @GetMapping("/api/resource/getcount")//获取所有房间号 以及对应容量
    List<RoomCapacityDTO> getCount();
    @PostMapping("/api/resource/upderedis")
    void updateresource(@RequestBody List<SeatReservation> seatReservation);
}
