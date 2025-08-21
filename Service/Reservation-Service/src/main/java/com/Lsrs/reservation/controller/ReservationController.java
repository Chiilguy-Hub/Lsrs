package com.Lsrs.reservation.controller;

import com.Lsrs.reservation.feign.NoticeFeignClient;
import com.Lsrs.reservation.feign.ResourceFeignClient;
import com.Lsrs.reservation.feign.UserFeignClinet;
import com.Lsrs.reservation.pojo.Echartsdata;
import com.Lsrs.reservation.pojo.SeatVO;
import com.Lsrs.reservation.pojo.panelsDao;
import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.example.Dao.TimeDao;
import com.example.Dao.scoreDao;
import com.example.pojo.PageResult;
import com.example.pojo.SeatReservation;
import com.Lsrs.reservation.service.Reservationservice;
import com.example.Dao.RoomCapacityDTO;
import com.example.pojo.Result;
import com.example.pojo.User;
import com.example.redis.RedisService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/reservation")

public class ReservationController {
    @Autowired
    Reservationservice reservationservice;
    @Autowired
    ResourceFeignClient resourceFeignClient;
    @Autowired
    NoticeFeignClient noticeFeignClient;
    @Autowired
    UserFeignClinet userFeignClinet;
    @Autowired
   private RedissonClient redissonClient;
 // 微信前端相关接口
    @GetMapping("/getscore/{userId}")//获得自己的总预约时长
    public Result getscore(@PathVariable String userId) {
        Integer score = reservationservice.getscore(userId);
        return Result.success(score);
    }
    @GetMapping("/getscore50")//获得前50的预约时长
    public Result getscore50() {
        List<scoreDao> scoreDaos = reservationservice.getscore50();
        //向user服务器请求基础信息
        List<scoreDao> scoreDaos1 = userFeignClinet.reservationCheckin(scoreDaos);
        return Result.success(scoreDaos1);
    }

    @GetMapping("/cancel/{userId}")
    public Result cancel(@PathVariable String userId) {
        SeatReservation getreservation = reservationservice.getreservation(userId);
        System.out.println(getreservation);
        List<SeatReservation> seatReservations=new ArrayList<>();
        int oldstatus = getreservation.getStatus();
        getreservation.setStatus(2);
        seatReservations.add(getreservation);
        System.out.println(seatReservations);

        resourceFeignClient.updateresource(seatReservations);
        reservationservice.cancel(userId,2, oldstatus); //更新状态
        noticeFeignClient.reservationCheckin(seatReservations);
         return  Result.success();
    }

    @GetMapping("/getallReservation/{userId}")
    public Result getallReservation(@PathVariable String userId) {

       List< SeatReservation> getreservation = reservationservice.getallReservation(userId);
        return Result.success(getreservation);
    }

  @GetMapping("/getreservation1/{userId}")
  public Result getReservation(@PathVariable String userId) {
      //获取最新的一条预约
      SeatReservation getreservation = reservationservice.getnewreservation(userId);
      return Result.success(getreservation);
  }
    @GetMapping("/getReservationnew/{userId}")
    public SeatReservation getReservationnew(@PathVariable String userId) {
        //获取最新的一条预约
        SeatReservation getreservation = reservationservice.getnewreservation(userId);
      return getreservation;
    }

@GetMapping("/getoften/{userId}")
    public Result getoften(@PathVariable String userId) {
    //获取常用座位列表
    List<RoomCapacityDTO> roomlist=reservationservice.getoftenreservation(userId);
    System.out.println(roomlist);
    return Result.success(roomlist);
}


@GetMapping("/seat")

public Result seat(  @RequestParam String zoneNumber,
                     @RequestParam String zone,
                     @RequestParam @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss") LocalDateTime startTime,
                     @RequestParam @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss") LocalDateTime endTime) {
    System.out.println(zoneNumber);
    System.out.println(startTime);
    System.out.println(endTime);
    System.out.println(zone);
    //获取所有的位置
    List <String> allSeats = resourceFeignClient.getAllRoom(Integer.parseInt(zoneNumber), zone);
    System.out.println(allSeats+"所有");
    //获取已经预约的座位
    List <String> getseats = reservationservice.getseats(Integer.parseInt(zoneNumber), startTime, endTime,zone);
    System.out.println(getseats+"已预约");
    // 转换已预约列表为集合
    Set<String> reservedSet = new HashSet<>(getseats);
    // 生成状态表
    List<SeatVO> statusList = new ArrayList<>();
    for (String seatId : allSeats) {
        boolean occupied = !reservedSet.contains(seatId);
        statusList.add(new SeatVO(seatId, occupied));
    }
    return  Result.success(statusList);
}
 /*
/  接收 预定信息
 */
 @SentinelResource(value = "seat_reservation") //sentinel
    @PostMapping ("/submit")
    //预约信息录入
    public Result reservation(@RequestBody SeatReservation   reservation ) {
     //判断是否在黑名单内  向user服务访问是否在其中
    if( userFeignClinet.getblacklist(reservation.getUserId())){
        return Result.fail(303,"黑名单内");
    }

     //获取到信息
        System.out.println(reservation.toString());
        SeatReservation seatReservation = reservationservice.getreservation(reservation.getUserId());
        int  Status=seatReservation.getStatus();
            if( seatReservation!=null&&(Status==1||Status==3)) { //1是使用状态 3是签到完
                System.out.println(1);
                return  Result.fail(302,"请完成预约后再次预约");
        }

     // 获取 seatNumber 以及 zoneNumber 生成对应的锁
     String lock_key= reservation.getZoneNumber()+"-"+reservation.getSeatNumber();
     System.out.println("lock_key:"+lock_key);
     //redis分布式锁
     RLock lock = redissonClient.getLock(lock_key);
     try {
         // 尝试获取锁（非阻塞）
         if (lock.tryLock(0, 5, TimeUnit.SECONDS)) {
             // 业务逻辑
             reservation.setCreatedAt(LocalDateTime.now());
             reservation.setStatus(1);
             reservation.setUpdatedAt(LocalDateTime.now());
             //向 resource 发出请求 更新容量
             resourceFeignClient.updateresource(List.of(reservation));
             //生成预约订单
             reservationservice.reservation(reservation);
             //向 notice 请求发送预约成功请求
             noticeFeignClient.reservationnotice(reservation);
             return Result.success();
         } else {
             return Result.fail(200,"系统繁忙");
         }
     } catch (InterruptedException e) {
         Thread.currentThread().interrupt();
         return Result.fail(200,"系统繁忙");
     } finally {
         if (lock.isHeldByCurrentThread()) {
             lock.unlock();
         }
     }

    }
/*
 完成定时任务 去初始化redis 时间段
 */
    @PostMapping("/loadredis")
    Result loadRedis(){
        List<RoomCapacityDTO> count = resourceFeignClient.getCount(); //获取到对应自习室容量
        System.out.println(count.toString());
       reservationservice.setInitialize(count);//查询预约订单 更新自习室容量
       return Result.success();
    }

  /*
  /   自动更新预约数据库内数据状态
         主要清理超时  未签到数据
   */

    //1、首先查询 在当前时间下过期未签到的
    //2. 查询当前时间下提前15分钟时通知要签到
    //3. 时间开始15分钟后还没有签到则自动取消
    //4. 自动完成用户过时
  @GetMapping("/updatestate_1")
  void updatestate_1(){ //自动签退
      //自动修改所有签到后订单的状态 变为完成状态4   （1预约中、2取消、3完成签到、4、完成 5、逾约 6 、进行中）
      reservationservice.update_outtime();
      //检查是否有
      List<SeatReservation> seatReservations = reservationservice.select_status4();
      System.out.println(seatReservations);
      //调用user 更新预约次数
       userFeignClinet.updataShuju(seatReservations);
      resourceFeignClient.updateresource(seatReservations);
  }
    @GetMapping("/updatestate_2")
  void updatestate_2(){ //签到提示
      //提前5分钟通知
       LocalDateTime checkdate = LocalDateTime.now().plusMinutes(5); //检查时间为当前时间+5分钟
      List<SeatReservation> seatReservations = reservationservice.notice_checkin(checkdate);
      if(seatReservations.size()>0) {
           //调用方法请求去通知
          System.out.println(seatReservations);
          noticeFeignClient.reservationCheckin(seatReservations);
      }

  }
  @GetMapping("/updatestate_3")
  void updatestate_3(){ //逾期检测
      // 减少五分钟
      LocalDateTime checkdate = LocalDateTime.now().minusMinutes(5); //检查时间为当前时间-5
      List<SeatReservation> seatReservations = reservationservice.notice_check(checkdate);
      //将需要发送信息发回
      System.out.println(seatReservations);
      userFeignClinet.updataShuju(seatReservations);
      noticeFeignClient.reservationCheckin(seatReservations);
      resourceFeignClient.updateresource(seatReservations);

  }
  @GetMapping("/updatecheck/{userId}")
  public void updatecheck(@PathVariable String userId) {
       reservationservice.cancel(userId,6,3);
  }

  // vue3 前端页面相关接口

    /* 前端4个panelsDao
    / 1、预约量 总预约数 日   2、完成预约量  完成率 周 3、逾约量 总预约量 月  4、预约用户数  总用户数
      */
    @PostMapping("/getpanelsdata")
    public Result getpanelsdata(){

        List<panelsDao> list = reservationservice.initPanels();//里面只有三条
        // 剩下的用户量 通过user
        LocalDate date = LocalDate.now();
        LocalDateTime startTime=date.atTime(0,0); //起始时间
        LocalDateTime endTime=date.atTime(23,59);//结束时间
        TimeDao timeDao = new TimeDao(startTime, endTime);
        Integer getcount = userFeignClinet.getcount(timeDao);// 当天人数
        TimeDao timeDao1 = new TimeDao();
        timeDao1.setStartTime(null);
        timeDao1.setEndTime(null);
        Integer getcount1 = userFeignClinet.getcount(timeDao1);//所有人数
        System.out.println(getcount1);
        panelsDao panelsDao=new panelsDao();
        panelsDao.setTitle("新增用户量");
        panelsDao.setValue(getcount);
        panelsDao.setUnit("日");
        panelsDao.setUnitColor("warning");
        panelsDao.setSubTitle("总用户量");
        panelsDao.setSubValue(String.valueOf(getcount1));
        panelsDao.setSubUnit("");
        list.add(panelsDao);
        Map<String,List<panelsDao>> map=new HashMap<>();
        map.put("panels",list);
        return Result.success(map);
    }
    /*
      / 获取echarts所需数据
     */
    @GetMapping("/getechartsdata")
    public Result getechartsdata(@RequestParam("type") String type){
       Map<String,List<String>>  map= reservationservice.getechartsdata(type);
       return Result.success(map);
    }

    @GetMapping("/getechartsdata1")//获取第二个echart 数据
    public Result getechartsdata1(@RequestParam("number") Integer number){
        Echartsdata echartsdata = reservationservice.getechartsdata1(number);
        return Result.success(echartsdata);
    }
    //前端获取reservation List
    @GetMapping("/getreservation/{page}")
    public Result<PageResult> getreservation(@PathVariable Integer page,
                                             @RequestParam(value ="pageSize"  , defaultValue = "10",required = false) Integer limit,
                                             @RequestParam(value ="tab"  ,required = false) Integer tab,
                                             @RequestParam(value ="startTime"  ,required = false )   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                 LocalDateTime startTime,
                                             @RequestParam( value ="endTime", required = false)   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                 LocalDateTime endTime){
        if(tab==0){
            tab=null;
        }
        System.out.println(startTime);
        PageResult<SeatReservation> result =  reservationservice.getReservationList(page, limit, startTime,endTime,tab);
        return Result.success(result);
    }

}
