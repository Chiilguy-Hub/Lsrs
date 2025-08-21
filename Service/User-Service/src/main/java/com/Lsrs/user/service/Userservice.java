package com.Lsrs.user.service;


import com.example.pojo.PageResult;
import com.example.Dao.scoreDao;
import com.example.pojo.SeatReservation;
import com.example.pojo.User;

import java.time.LocalDateTime;
import java.util.List;

public interface Userservice {

    User getUserByOpenid(String openid);
    void updateUserByOpenid(User user);

    List<scoreDao> getuser(List<scoreDao> users);

    Integer getusercount(LocalDateTime startTime, LocalDateTime endTime);

    PageResult<User> getUserList(Integer page, Integer limit, String userId,String realName,String phone);

    void updateUserByUserId(User user);

    void delete(String userId);

    void updatashuju(List<SeatReservation> seatReservations);
}
