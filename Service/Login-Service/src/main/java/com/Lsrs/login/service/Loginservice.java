package com.Lsrs.login.service;

import com.example.pojo.User;
import org.springframework.web.bind.annotation.RequestParam;


public interface Loginservice {
//    void insert(User user);
    //获取open_id
  String getSession(String appid, String secret, String js_code, String grant_type);
    //通过open_id 向user服务器查询用户信息
    User getUserByOpenid(@RequestParam String openid);
}


