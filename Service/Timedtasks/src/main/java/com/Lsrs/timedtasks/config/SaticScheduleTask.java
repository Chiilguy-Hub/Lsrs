package com.Lsrs.timedtasks.config;

import com.Lsrs.timedtasks.feign.ReservationFeign;
import com.Lsrs.timedtasks.feign.ResourceFeign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;


@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class SaticScheduleTask {
    @Autowired
    static ResourceFeign resourceFeign;

    @Autowired
    static ReservationFeign reservationFeign;

    @Scheduled(cron = "0 30 6 * * *")
  public void taskAt6(){
        reservationFeign.loadRedis();
    }

    // 7:45 到 21:55，每5分钟一次
    @Scheduled(cron = "0 45/5 6-21 * * ?") // 每5分钟
    public void task7amTo9pm() {
        reservationFeign.updatestate_3();//检查是否逾期
        reservationFeign.updatestate_2();//提示签到
        reservationFeign.updatestate_1();//自动签退
        System.out.println("Task executed at: " + new Date());
    }

    // 22:00 和 22:05 执行
    @Scheduled(cron = "0 0,5 22 * * ?")
    public void taskAt10PM() {
        reservationFeign.updatestate_3();//检查是否逾期
        reservationFeign.updatestate_2();//提示签到
        reservationFeign.updatestate_1();//自动签退
        System.out.println("Task executed at: " + new Date());
    }


}
