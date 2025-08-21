package com.Lsrs.check_in.feign;

import com.Lsrs.check_in.Pojo.Qr;
import com.example.Dao.QrRequest;
import com.example.Dao.RoomCapacityDTO;
import com.example.pojo.SeatReservation;
import com.example.pojo.room;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@FeignClient(value = "Resource-Service")//服务器的名称           //feign 客户端
public interface ResourceFeignClient { //业务间api调用
    //1、标注在Controller上，是接受请求。
    //2.标注在Feign上，是发送请求。
 @PostMapping("/api/resource/getseat")
 List<QrRequest> getseat(@RequestBody QrRequest qrRequest);

}
