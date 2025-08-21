package com.Lsrs.login.service.impl;



import com.Lsrs.login.feign.LoginFeignClient;
import com.Lsrs.login.feign.UserFeignClient;


import com.Lsrs.login.service.Loginservice;
import com.example.pojo.Result;
import com.example.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;




@Service
public class LoginserviceImpl implements Loginservice {


  @Autowired
   LoginFeignClient loginFeignClient; //微信服务器发送请求

  @Autowired
    UserFeignClient userFeignClient; //向user服务器发送请求


    /*
    *   获取来自微信的session
    * */

    @Override
    public String getSession(String appid, String secret, String js_code, String grant_type) {
        return loginFeignClient.login(appid,secret,js_code,grant_type);
    }


  /*
  /   根据openid 获取用户信息
   */
    @Override
    public User getUserByOpenid(String openid) {
        Result userByOpenid = userFeignClient.getUserByOpenid(openid);
        return  (User) userByOpenid.getData();
    }
}
