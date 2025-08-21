package com.Lsrs.login.controller;
import com.Lsrs.login.pojo.SessionResult;
import com.example.Until.JsonUtils;
import com.example.Until.JwtUntil;
import com.example.pojo.Result;
import com.Lsrs.login.service.Loginservice;

import com.example.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/api/weixin")

public class LoginController {
@Autowired
  private   Loginservice loginservice;

@Autowired
 private RedisService redisService;
@PostMapping("/adminlogin")
public Result adminlogin( @RequestBody Map<String, String> params){
    String username = params.get("username");
    String password = params.get("password");
    System.out.println(username);
    System.out.println(password);
    if(username.equals("admin") && password.equals("123456")){
     String uuid = UUID.randomUUID().toString();//生成一个uuid与
     Map<String, Object> claim = new HashMap<>();
     claim.put("uuid", uuid);
     String  token = JwtUntil.creatJwt(claim);
     Map<String, Object> map = new HashMap<>();//将token存入map、
        redisService.set("uuid:"+uuid,token,3600*2);
     map.put("token",token);
     return Result.success(map);
 }else {
     return Result.fail(401,"密码错误");
 }
}


@GetMapping("/login/{code}")
//获取微信端传回的临时code
    public Result login(@PathVariable String code) {
    System.out.println(code);

    String appid="wxf4d9d3d641361fc7";
    String secret="351105e45cea2b6cc0ede4d96690579e";
    String grant_type="authorization_code";

//
    String session = loginservice.getSession(appid, secret, code, grant_type);//获取open_id
    SessionResult sessionResult = JsonUtils.jsonToPojo(session, SessionResult.class);
    System.out.println(sessionResult.toString());
    //判断open_id对应的信息是否过期
  String  token = redisService.get("open_id:"+sessionResult.getOpenid());//获取对应的token
    System.out.println(token);
    if(token==null){ //如果用户token过期或着没有 则重新注册
        //生成新Token
        String uuid = UUID.randomUUID().toString();//生成一个uuid与
        Map<String, Object> claim = new HashMap<>();
        claim.put("uuid", uuid);
        token = JwtUntil.creatJwt(claim);
        System.out.println(token);
        //将uuid 与open_id绑定 (uuid唯一 故每一个uuid其实可以对应一个用户open_id)
          redisService.set("uuid:"+uuid,sessionResult.getOpenid(),3600*2);
        //将Open_id与 token绑定
        redisService.set("open_id:"+sessionResult.getOpenid(),token,3600*2);//将数据存入redis中
        System.out.println(token);
    }
    Map<String, Object> map = new HashMap<>();//将token存入map
    map.put("token",token);
    return Result.success(map);
}

}
