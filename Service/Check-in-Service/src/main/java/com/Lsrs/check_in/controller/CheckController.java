package com.Lsrs.check_in.controller;

import com.Lsrs.check_in.Pojo.Qr;
import com.Lsrs.check_in.feign.NoticeFeignClient;
import com.Lsrs.check_in.feign.ReservationFeignClient;
import com.Lsrs.check_in.feign.ResourceFeignClient;
import com.Lsrs.check_in.service.Check_inService;
import com.example.Dao.QrRequest;
import com.example.pojo.Result;
import com.example.pojo.SeatReservation;
import com.example.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/check-in")
public class CheckController {
    @Autowired
    ResourceFeignClient resourceFeignClient;
    @Autowired
    Check_inService check_inService;
    @Autowired
    private RedisService redisService;
    @Autowired
    ReservationFeignClient reservationFeignClient;
    @Autowired
    NoticeFeignClient noticeFeignClient;
    @PostMapping("/generate-qr")
    public Result createQR(@RequestBody QrRequest qrRequest) {
        System.out.println(qrRequest);
        List<Qr> qrs = new ArrayList<>();
        //获取对应的数据
        List<QrRequest> getseat = resourceFeignClient.getseat(qrRequest);
        System.out.println(getseat);
        List<Qr> creatqr = check_inService.creatqr(getseat);
        return Result.success(creatqr);
    }

    @PostMapping("/checkqr")
    public Result checkqr(@RequestBody QrRequest qrRequest) {
        System.out.println(qrRequest);
        String userId = qrRequest.getUserId();
        String  file=qrRequest.getSeatFloor()+"-"+qrRequest.getZoneNumber()+"-"+qrRequest.getSeatNumber();
        String s = redisService.get(file);
        if(s!=null&&s.equals(qrRequest.getUuid())){
            //修改预定信息
            SeatReservation reservation = reservationFeignClient.getReservationnew(userId);
            noticeFeignClient.reservationCheckin(List.of(reservation));
            reservationFeignClient.updatecheck(userId);
            return Result.success();
        }
        return Result.fail(304,"扫码错误");
    }
}
