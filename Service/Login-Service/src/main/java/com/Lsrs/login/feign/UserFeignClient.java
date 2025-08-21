package com.Lsrs.login.feign;

import com.example.pojo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient( value= "User-Service" ,url = "http://api/user")
public interface UserFeignClient {
     @GetMapping("/getuserByopenid")
     Result getUserByOpenid(@RequestParam String openid);
}
