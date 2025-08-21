package com.Lsrs.user.service.impl;

import com.Lsrs.user.Mapper.UserMapper;
import com.example.pojo.PageResult;
import com.Lsrs.user.pojo.scdao;
import com.Lsrs.user.service.Userservice;

import com.example.Dao.scoreDao;
import com.example.pojo.SeatReservation;
import com.example.pojo.User;
import com.example.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserserviceImpl implements Userservice {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    RedisService redisService;

    @Override
    public User getUserByOpenid(String openid) {
       User user = (User) userMapper.selectById(openid);
           if(user==null){
               user = new User();
               user.setCreatedAt(LocalDateTime.now());
               user.setWechatOpenid(openid);
               user.setUserImg("/assets/images/avatar.png");
               user.setUserName("未设置");
               userMapper.insert(user);
           }
           return user;
    }

    @Override
    public void updateUserByOpenid(User user) {
        System.out.println(user);;
        userMapper.updateById(user);
    }

    @Override
    public List<scoreDao> getuser(List<scoreDao> users) {
        for (scoreDao user : users) {
            String userId = user.getUserId();
            scdao getuser = userMapper.getuser(userId);
            user.setUserImg(getuser.getUserImg());
            user.setUserName(getuser.getUserName());
        }
        return users;
    }

    @Override
    public Integer getusercount(LocalDateTime startTime, LocalDateTime endTime) {
        return  userMapper.getusercount(startTime,endTime);
    }

    @Override
    public PageResult<User> getUserList(Integer page, Integer limit, String userId,String realName,String phone) {
             page = page == null ? 1 : page;
             Integer lim = (page-1)*limit;
             Integer lim1=page*limit;
            List<User>  list= userMapper.getUserList(lim, lim1, userId, realName, phone);
            Integer count=userMapper.getusercount(null,null);
        PageResult<User> userPageResult = new PageResult<>();
        userPageResult.setTotal(count);
        userPageResult.setList(list);
        return userPageResult;
    }

    @Override
    public void updateUserByUserId(User user) {
        userMapper.updateUserByUserId(user);
    }

    @Override
    public void delete(String userId) {
        userMapper.delete(userId);
    }

    @Override
    public void updatashuju(List<SeatReservation> seatReservations) {
        for (SeatReservation seatReservation : seatReservations) {
            String userId = seatReservation.getUserId();
            int status=seatReservation.getStatus();
            //完成
            if(status==4){
             userMapper.updatetotal(userId);
            }else if(status==5){
                //逾期
                Integer getviolation = userMapper.getviolation(userId);
                if (getviolation==2){
                    userMapper.violation(userId,0);
                    redisService.set(userId,"3",60*60*24*3);
                }else{
                    userMapper.violation(userId,getviolation+1);
                }
            }


        }
    }
}
