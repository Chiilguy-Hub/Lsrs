package com.Lsrs.resource.controller;


import com.Lsrs.resource.Dao.ReservationRequest;
import com.Lsrs.resource.pojo.room_seat;
import com.Lsrs.resource.service.ResourceService;
import com.example.Dao.QrRequest;
import com.example.Dao.RoomCapacityDTO;
import com.example.pojo.PageResult;
import com.example.pojo.Result;
import com.example.pojo.SeatReservation;
import com.example.pojo.room;
import com.example.redis.RedisService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
@RequestMapping("/api/resource")
@RestController
public class ResourceController{
  @Autowired
  ResourceService resourceService;

  @Autowired
  RedisService redisService;

@PostMapping("/floor")
    //查询对应楼层的自习室以及座位数
  public Result getResource(@RequestBody ReservationRequest request){
    System.out.println(request);
    String floor=request.getFloor();
  List<Map<String, Integer>> roombyfloor = resourceService.roombyfloor(floor);
  System.out.println(roombyfloor);
  List<RoomCapacityDTO> list=new ArrayList<>();//用来存储返回值
  for (Map<String, Integer> room : roombyfloor) {
    String zoneNumber = String.valueOf(room.get("zone_number"));
    LocalDateTime startTime=request.getStartTime();
    LocalDateTime endTime=request.getEndTime();
    // 401yy-mm-dd hh:mm:ss : count 存储
    int x=Integer.MAX_VALUE;
    while (startTime.isBefore(endTime)) {
      Integer s = Integer.valueOf(redisService.get(zoneNumber + startTime.toString()));
      x=Math.min(x, s);
     startTime= startTime.plusMinutes(15);
    }

    //获取 startTime-endTime 时间段容量
    list.add(new RoomCapacityDTO(room.get("zone_number"),x));
  }
  System.out.println(list);
  return Result.success(list);//返回的数据类型 list< RoomCapacityDTO >
}


  @PostMapping("/getall")
  public List getAllRoom(@RequestParam("zone_number") int zone_number,@RequestParam("zone") String zone){
      return resourceService.getAllRoom(zone_number,zone);
  }

  @GetMapping("/getcount")
  public List<RoomCapacityDTO> getCount(){
    List<RoomCapacityDTO> res=resourceService.getcount();
    return res;
  }

  @PostMapping("/upderedis")
  void updateresource(@RequestBody List<SeatReservation> seatReservation){
    System.out.println(seatReservation);
    for (SeatReservation seatReservation1 : seatReservation) {
      LocalDateTime startTime = seatReservation1.getStartTime();
      LocalDateTime endTime = seatReservation1.getEndTime();
      int zoneNumber = seatReservation1.getZoneNumber();
      int status = seatReservation1.getStatus();
      System.out.println(status);
      if(status==1){ //预定位置减少
        while (startTime.compareTo(endTime) <0){
          String key=String.valueOf(zoneNumber) + "" + startTime.toString();  
          String s = redisService.get(key);
          Integer i = Integer.valueOf(s);
          i--;
          Long expire = redisService.getExpire(key);
          System.out.println(expire);
          redisService.set(key, i.toString(), expire.intValue());
          startTime = startTime.plusMinutes(15);
        }
      }else if(status==2||status==5||status==4){ //取消或着逾期 归还
        while (startTime.compareTo(endTime) < 0){
          String key=String.valueOf(zoneNumber) + "" + startTime.toString();
          String s = redisService.get(key);
          Integer i = Integer.valueOf(s);
          i++;
          Long expire = redisService.getExpire(key);
          System.out.println(expire);
          redisService.set(key, i.toString(), expire.intValue());
          startTime = startTime.plusMinutes(15);
        }

      }

    }
    }

  @GetMapping("/getresource/{page}")
  public Result<PageResult> getresource(@PathVariable Integer page,
                                        @RequestParam(value ="Pagesize"   , defaultValue = "20",required = false) Integer limit,
                                        @RequestParam(value = "title" , required = false) Integer zoomNumber) {

    PageResult<room_seat> room = resourceService.getROOM(page, limit, zoomNumber);

    return Result.success(room);
  }
@PostMapping("/create")
  public Result create(@RequestBody room_seat room) {
  System.out.println(room);
  resourceService.insert(room);
  return  Result.success(room);
}
@PostMapping("/update/{id}")
  public Result update(@RequestBody room_seat room, @PathVariable Integer id) {
  System.out.println(room);
  resourceService.update(room);
  return  Result.success();
}

@PostMapping("/delete/{id}")
  public Result delete(@PathVariable Integer id) {
    System.out.println(id);
    resourceService.delete(id);
    return Result.success();
}
  @PostMapping("/getseat")
  List<QrRequest> reservation(@RequestBody QrRequest qrRequest){

     List<QrRequest> getqrseat = resourceService.getqrseat(qrRequest);
     return getqrseat;
  }


}
