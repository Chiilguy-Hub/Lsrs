package com.Lsrs.login.feign;



import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;




@FeignClient(  name="weixinApi",url = "https://api.weixin.qq.com")
public interface LoginFeignClient {
    //访问
    @GetMapping(  value = "/sns/jscode2session") // 强制指定消费 JSON
    String login(@RequestParam("appid") String appid, @RequestParam("secret") String secret, @RequestParam("js_code") String js_code, @RequestParam("grant_type") String grant_type);
}
