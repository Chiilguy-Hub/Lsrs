package com.Lsrs.notice.service.Impl;
import java.time.LocalDateTime;
import java.util.Date;

import com.Lsrs.notice.Mapper.NoticeMapper;
import com.Lsrs.notice.pojo.SeatReservation;
import com.Lsrs.notice.pojo.UserNotification;
import com.Lsrs.notice.service.Noticeservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeserviceImpl implements Noticeservice {
@Autowired
    NoticeMapper noticeMapper;

    @Override
    public void sendnotice(List<SeatReservation> seatReservations) {
         //根据预约表完成信息 填入数据库中
    seatReservations.forEach(seatReservation -> {
        //分开处理每一个通知
        UserNotification userNotification = new UserNotification();
        userNotification.setUserId(seatReservation.getUserId());
        userNotification.setZoneNumber(seatReservation.getZoneNumber());
        userNotification.setSeatNumber(seatReservation.getSeatNumber());
        userNotification.setCreatedAt(LocalDateTime.now());
        userNotification.setUpdatedAt(LocalDateTime.now());
        String content="您预约的"+seatReservation.getZoneNumber()+"号自习室"+seatReservation.getSeatNumber()+"座位";
        int status = seatReservation.getStatus();
        System.out.println(status);
        int  notice_status=0;       // 状态有五种 需要处理的有 2、取消  3、签到 4、完成 5、逾约
        switch (status) { //1、为签到通知  2、取消预约通知 3、为签到成功通知 4、逾约通知
            case 1:
          content+="将在5min后进行签到，请您在签到时长5min。";
          notice_status=2;
                break;
            case 2:
            content+="已被取消,请合理规划预约时间。";
                notice_status=1;
                break;
            case 3:
            content+="已成功签到，祝您学习愉快。";
                notice_status=3;
                break;
            case 5:
            content+="未在规定时间内签到，已取消预约。";
                notice_status=4;
                break;
        }
        userNotification.setContent(content);
        userNotification.setType(notice_status);
       noticeMapper.insert(userNotification);

    });




    }

    @Override
    public List<UserNotification> getnoticebyId(String userId) {
        return noticeMapper.selectByuserId(userId);
    }

    @Override
    public void sendreservationnotice(SeatReservation seatReservation) {
        UserNotification userNotification = new UserNotification();
        userNotification.setUserId(seatReservation.getUserId());
        userNotification.setZoneNumber(seatReservation.getZoneNumber());
        userNotification.setSeatNumber(seatReservation.getSeatNumber());
        userNotification.setCreatedAt(LocalDateTime.now());
        userNotification.setUpdatedAt(LocalDateTime.now());
        String content="您成功预约到了"+seatReservation.getZoneNumber()+"号自习室"+seatReservation.getSeatNumber()+"座位请及时到达！";
        userNotification.setContent(content);
        userNotification.setType(0);
        noticeMapper.insert(userNotification);
    }
}

