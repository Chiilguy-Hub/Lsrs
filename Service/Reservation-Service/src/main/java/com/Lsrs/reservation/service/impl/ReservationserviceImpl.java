package com.Lsrs.reservation.service.impl;
import com.Lsrs.reservation.Mapper.ReservationMapper;
import com.Lsrs.reservation.feign.ResourceFeignClient;
import com.Lsrs.reservation.pojo.Echartsdata;
import com.Lsrs.reservation.pojo.StudyRoomData;
import com.Lsrs.reservation.pojo.panelsDao;
import com.example.Dao.scoreDao;
import com.example.pojo.PageResult;
import com.example.pojo.SeatReservation;
import com.Lsrs.reservation.service.Reservationservice;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.Dao.RoomCapacityDTO;
import com.example.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ReservationserviceImpl implements Reservationservice {

@Autowired
private ReservationMapper reservationMapper;

    @Autowired
    private ResourceFeignClient resourceFeignClient;
    @Autowired
    private RedisService redisService;

    //将预约信息存入数据库
    @Override
    public String reservation(SeatReservation appointment) {
        //将预约信息写回数据库  Reservation
        reservationMapper.insert(appointment);
        return appointment.toString();
    }
    /*
      获取 一个自习室内所有座位
     */
    @Override
    public List getseats(int zone_number, LocalDateTime startTime, LocalDateTime endTime,String zone) {
        LambdaQueryWrapper<SeatReservation> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(SeatReservation::getSeatNumber)
                .eq(SeatReservation::getZoneNumber, zone_number)
                .in(SeatReservation::getStatus, Arrays.asList(1, 3))
                // 时间冲突条件组
                .and(wq -> wq
                        .between(SeatReservation::getStartTime, startTime, endTime)
                        .or()
                        .le(SeatReservation::getStartTime, startTime)
                        .ge(SeatReservation::getEndTime, endTime)
                )
                // 独立的模糊查询条件
                .likeRight(SeatReservation::getSeatNumber, zone)  // 使用右模糊查询
                .groupBy(SeatReservation::getSeatNumber);
        // 2. 执行查询并提取 seatNumber
        return reservationMapper.selectObjs(wrapper)  // 返回 List<Object>
                .stream()
                .map(obj -> (String) obj)
                .collect(Collectors.toList());
    }
   //根据学号获取预约信息
    @Override
    public SeatReservation getreservation(String userId) {
         SeatReservation seatReservation = reservationMapper.selectBy_Id(userId);
         if (seatReservation == null) {
             return new SeatReservation();
         }else{
             return seatReservation;
         }
    }

     /*
     /   初始化时间段内 不同自习室容量 
      */
    @Override
    public void setInitialize(List<RoomCapacityDTO> total) {
     for(int i=0;i<3;i++){ //初始化三天
         LocalDate date = LocalDate.now().plusDays(i);
         LocalDateTime startTime = date.atTime(8,0);
         System.out.println(startTime);
         List<LocalDateTime> localDateTimes = generateTimeSlots(date);//生成的当天对应的时间段


         // 准备通过zone_number 按时间段查询
         for (RoomCapacityDTO it : total) {
             Integer zoneNumber = it.getZoneNumber();
             Integer count = it.getCount();

             localDateTimes.forEach(localDateTime -> { //各个时间段
                 Integer TimeStage_count = reservationMapper.selecttime(localDateTime, zoneNumber);
                 System.out.println(count-TimeStage_count);
                 redisService.set( zoneNumber.toString()+localDateTime.toString(), String.valueOf(count-TimeStage_count),3600*24);//座位信息只保存1天
                 //将剩余座位量以 例如 401yy-mm-dd hh:mm:ss : count 存储
             });
             redisService.set(zoneNumber.toString(),count.toString(),3600*24);
         }


     }


    }

    @Override
    public void update_outtime() {
        //用户已经签到 所以 status 为3 的为目标订单
        reservationMapper.update_outtime();
    }

    @Override
    public List<SeatReservation> notice_checkin(LocalDateTime checkdate) {

        List<SeatReservation> seatReservations = reservationMapper.notice_checkin(checkdate);
        return seatReservations;
    }

    @Override
    public List<SeatReservation> select_status4() {
        return reservationMapper.select_status4();

    }

    @Override
    public List<SeatReservation> notice_check(LocalDateTime checkdate) {
        List<SeatReservation> seatReservations = reservationMapper.notice_check(checkdate);
        //发送信息之前还需要把预约表状态修改
        for (SeatReservation seatReservation : seatReservations) {
            String userId = seatReservation.getUserId();
            reservationMapper.updatebyid(userId,5,3);
            seatReservation.setStatus(5);
        }
        return seatReservations;
    }

    @Override
    public SeatReservation getnewreservation(String userId) {
       return reservationMapper.getnewreservation(userId);
    }

    @Override
    public List<RoomCapacityDTO> getoftenreservation(String userId) {
        return reservationMapper.getoftenreservation(userId);

    }

    @Override
    public List<SeatReservation> getallReservation(String userId) {
        return reservationMapper.getallReservation(userId);
    }

    @Override
    public Integer getscore(String userId) {
        return reservationMapper.getscore(userId);
    }


    @Override
    public List<scoreDao> getscore50() {
        List<scoreDao> scoreDaos = new ArrayList<>();
        scoreDao scoreDao = new scoreDao();
        Integer getscore = reservationMapper.getscore("210501050011");
        scoreDao.setUserId("210501050011");
        scoreDao.setCount(getscore);
        for (int i = 0; i < 50; i++) {
            scoreDaos.add(scoreDao);
        }
        return scoreDaos;
    }

    @Override
    public void cancel(String userId, int i,int oldStatus) {
         reservationMapper.updatebyid(userId,i,oldStatus);
    }



    @Override
    public List<panelsDao> initPanels() {
        List<panelsDao>  panels=new ArrayList<>();
    // 1、生成第一个先获取当天的所有预约 再获取总的预约量
        LocalDate date = LocalDate.now();
        LocalDateTime startTime=date.atTime(7,0); //起始时间
        LocalDateTime endTime=date.atTime(22,0);//结束时间
        Integer count = reservationMapper.getreseRvationCountbytime(startTime, endTime,null,null);
        Integer allcount=reservationMapper.getreseRvationCountbytime(null, null,null,null);
        panelsDao panelsDao = new panelsDao();
        panelsDao.setTitle("预约量");
        panelsDao.setValue(count);
        panelsDao.setUnit("日");
        panelsDao.setUnitColor("success");
        panelsDao.setSubTitle("总预约量");
        panelsDao.setSubValue(String.valueOf(allcount));
        panelsDao.setSubUnit("");
        panels.add(panelsDao);
    //2. 完成预约量 周   1680
        //七天前
        LocalDateTime day7startTime = startTime.minusDays(6);

        Integer count7 = reservationMapper.getreseRvationCountbytime(day7startTime, endTime, 4,null);//从7天前的7点到现在的22点
        System.out.println(count7+"------"+day7startTime);
        System.out.println(count7+"------"+endTime);

        Integer countall7=reservationMapper.getreseRvationCountbytime(day7startTime, endTime,null,null);
        panelsDao panelsDao1 = new panelsDao();
        panelsDao1.setTitle("完成量");
        panelsDao1.setValue(count7);
        panelsDao1.setUnit("周");
        panelsDao1.setUnitColor("danger");
        panelsDao1.setSubTitle("完成率");
        Integer value=0;
        if(countall7!=0){
            value=count7*100/countall7;
        }
        panelsDao1.setSubValue(String.valueOf(value)+'%');
        panelsDao1.setSubUnit("");
        panels.add(panelsDao1);
    //3. 逾约量  总预约率     月
        LocalDateTime day30startTime = startTime.minusDays(29);//三十天前
        Integer count30 = reservationMapper.getreseRvationCountbytime(day30startTime, endTime, 5,null);//30天到现在的逾约量
        Integer countall30 = reservationMapper.getreseRvationCountbytime(day30startTime, endTime, null,null);//30天所有预约量
        panelsDao panelsDao2 = new panelsDao();
        panelsDao2.setTitle("逾约量");
        panelsDao2.setValue(count30);
        panelsDao2.setUnit("月");
        panelsDao2.setUnitColor("primary");
        panelsDao2.setSubTitle("总逾约率");
        Integer value1 = 0;
        if(count30!=0){
            value1 = count30*100/countall30;
        }
        panelsDao2.setSubValue(String.valueOf(value1)+'%');
        panelsDao2.setSubUnit("");
        panels.add(panelsDao2);
        return  panels;
    }
   /*
   /    获取echarts对应数据
     */
    @Override
    public Map<String, List<String>> getechartsdata(String type) {
        Map<String, List<String>> map = new HashMap<>();
        List<String> x=new ArrayList<>();  // ECHARTS 对应的x轴
        List<String> y=new ArrayList<>(); //    y 轴
        LocalDate date = LocalDate.now(); //获取当天日期
        LocalDateTime endTime = date.atTime(23, 59);
        LocalDateTime startTime = date.atTime(0, 0);
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("MM-dd");
        if(type.equals("hour")){ //
            LocalDateTime startTimeday = date.atTime(0, 0);
            LocalDateTime endTimeday = startTimeday.plusHours(1);
            // 当天
            for(int i=0;i<23;i++){
                Integer count = reservationMapper.getreseRvationCountbycreattime(startTimeday, endTimeday);

                if(i<10){
                    x.add("0"+String.valueOf(i));
                }else {
                    x.add(String.valueOf(i));
                }
                y.add(String.valueOf(count));
                startTimeday=startTimeday.plusHours(1);
                endTimeday = endTimeday.plusHours(1);
            }


        }else if (type.equals("week")) {
            LocalDateTime startTimeweek = startTime;
            LocalDateTime endTimeweek = endTime;
            for(int i=6;i>=0;i--){
                LocalDateTime startTimeweek1 = startTimeweek.minusDays(i);// i天前的00：00
                LocalDateTime endTimeweek1 = endTimeweek.minusDays(i); // i天前的 23:59
                String monthDay = startTimeweek1.format(formatter1);
                x.add(monthDay);
                Integer i1 = reservationMapper.getreseRvationCountbycreattime(startTimeweek1, endTimeweek1);
                y.add(String.valueOf(i1));
            }

        }else if (type.equals("month")) {
            LocalDateTime startTimemonth = startTime;
            LocalDateTime endTimemonth = endTime;
            for(int i=29;i>=0;i--){
                LocalDateTime startTimemonth1 = startTimemonth.minusDays(i);// i天前的00：00
                LocalDateTime endTimemonth1 =endTimemonth.minusDays(i); // i天前的 23:59
                String monthDay = startTimemonth1.format(formatter1);
                x.add(monthDay);
                Integer i1 = reservationMapper.getreseRvationCountbycreattime( startTimemonth1,endTimemonth1);
                y.add(String.valueOf(i1));
            }

        }
        map.put("x",x);
        map.put("y",y);
        return map;
    }

    @Override
    public Echartsdata getechartsdata1(Integer count) {
        Echartsdata echartsdata = new Echartsdata();
      List<String> x=new ArrayList<>();
      List<StudyRoomData> y=new ArrayList<>();
      DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("MM-dd");
        LocalDate date = LocalDate.now(); //获取当天日期
        LocalDateTime endTime = date.atTime(23, 59);
        LocalDateTime startTime = date.atTime(0, 0);

       //1.获取 一周内预约量排名前5的自习室的
        LocalDateTime startTime1= startTime.minusDays(6); //七天前的 00:00
      List<Integer> list=  reservationMapper.getroomrank(startTime1,endTime,count); //获取这七天内预约最多的自习室号
        Integer value1 = list.get(0);
        for (Integer s : list) {
            StudyRoomData studyRoomData = new StudyRoomData();
            studyRoomData.setName(s+"室");
            List<Integer> list1=new ArrayList<>();
            for(int i=6;i>=0;i--){
              LocalDateTime startTime2=startTime.minusDays(i);
              LocalDateTime endTime2=endTime.minusDays(i);
              String monthDay = startTime2.format(formatter1);//日期
           if(value1==s){
              x.add(monthDay);
           }

              Integer i1 = reservationMapper.getreseRvationCountbytime(startTime2, endTime2, null, s);
              list1.add(i1);

          }
            studyRoomData.setData(list1);//数据放入
            y.add(studyRoomData);
        }
        echartsdata.setY(y);
        echartsdata.setX(x);

          return echartsdata;
    }

    @Override
    public PageResult<SeatReservation> getReservationList(Integer page, Integer limit,LocalDateTime startTime,LocalDateTime endTime, Integer tab) {
        PageResult<SeatReservation> seatReservationPageResult = new PageResult<>();
        Integer count=reservationMapper.getreseRvationCountbytime(startTime,endTime,tab,null);//获取对应的数量
        Integer limit1 = (page - 1) * limit;
        List<SeatReservation> list=reservationMapper.getReservation(limit1,startTime,endTime,tab);
        seatReservationPageResult.setList(list);
        seatReservationPageResult.setTotal(count);
        return seatReservationPageResult;
    }


    // 生成时间段方法
    public static List<LocalDateTime> generateTimeSlots(LocalDate date) {
        List<LocalDateTime> timeSlots = new ArrayList<>();

        // 定义起始和结束时间（结合传入的日期）
        LocalDateTime startTime = date.atTime(8, 0);   // 8:00
        LocalDateTime endTime = date.atTime(21, 0);    // 21:00
        LocalDateTime currentTime = startTime;
        // 循环生成时间点，直到超过结束时间
        while (!currentTime.isAfter(endTime)) {
            timeSlots.add(currentTime);
            currentTime = currentTime.plusMinutes(15); // 每次增加15分钟
        }

        return timeSlots;
    }
}
