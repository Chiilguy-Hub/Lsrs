package com.Lsrs.user.controller;

import com.example.pojo.PageResult;
import com.Lsrs.user.service.Userservice;
import com.Lsrs.user.untils.AliOssUtil;
import com.example.Dao.TimeDao;
import com.example.Dao.scoreDao;
import com.example.pojo.Result;
import com.example.pojo.SeatReservation;
import com.example.pojo.User;
import com.example.redis.RedisService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.Until.JwtUntil;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
    @RequestMapping("/api/user")
@Slf4j
public class UserController {
    @Autowired
    private Userservice userservice;
    @Autowired
    private RedisService redisService;
    @Autowired
    private AliOssUtil aliOssUtil;



    @GetMapping("/getuserInfo")
    Result getUserByOpenid(@RequestHeader("Authorization") String token) {
        Claims claims = JwtUntil.parseJwt(token);
        System.out.println(token);
        String uuid = (String) claims.get("uuid");
        String openid = redisService.get("uuid:"+uuid);
        User user= userservice.getUserByOpenid(openid);
        user.setWechatOpenid(uuid);
        System.out.println(user.toString());
        return Result.success(user);
    }
    /*/
      获取用户头像
     */
  @PostMapping("/uploadFile")
  Result uploadFile(@RequestParam("file") MultipartFile file) {
      log.info("文件上传：{}",file);
      try {
          //原始文件名
          String originalFilename = file.getOriginalFilename();
          //截取原始文件名的后缀.png .jpg
          String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
          //构造新文件名称
          String objectName = "qr/"+UUID.randomUUID().toString() + extension;
          //文件的请求路径
          String filePath = aliOssUtil.upload(file.getBytes(), objectName);
          System.out.println(filePath);
          return Result.success(filePath);
      } catch (IOException e) {
          log.error("文件上传失败：{}",e);
      }
      return Result.fail(402,"图片上传失败");
  }
  /*
   修改用户信息
   */
    @PostMapping("/updateuserInfo")
    Result updateUserInfo(@RequestBody User user) {
       user.setCreatedAt(LocalDateTime.now());
        String openid = redisService.get("uuid:" + user.getWechatOpenid());
        user.setWechatOpenid(openid);
        userservice.updateUserByOpenid(user);
        return Result.success();

    }
    /*
     获取部分用户信息
     */
    @PostMapping(("/getuser"))
    List<scoreDao> reservationCheckin(@RequestBody List<scoreDao> users){
       userservice.getuser(users);
        return users;
    }
    @PostMapping("/getcount")
    Integer getcount(@RequestBody TimeDao timeDao){
        //通过时间获取用户量
        LocalDateTime startTime = timeDao.getStartTime();
        LocalDateTime   endTime=timeDao.getEndTime();
        System.out.println(startTime+" "+endTime);
        Integer getusercount = userservice.getusercount(startTime, endTime);
        return getusercount;
    }

    /*
     /  前端数据请求
     */
    @GetMapping("/getUserlist/{page}")
    public Result<PageResult> getUserList(@PathVariable Integer page,
                                          @RequestParam( value ="userId", required = false) String userId,
                                          @RequestParam(value ="pageSize"  , defaultValue = "10",required = false) Integer limit,
                                          @RequestParam(value ="realName"  ,required = false) String realName,
                                          @RequestParam(value ="phone"  ,required = false) String phone){
        System.out.println(limit);
        PageResult<User> result =  userservice.getUserList(page, limit, userId,realName,phone);
      return Result.success(result);
    }
    @PostMapping("/updata")
    public Result updata(@RequestBody User user){
        System.out.println(user);
      userservice.updateUserByUserId(user);
        return  Result.success();
    }
    @PostMapping("/delete/{userId}")
    public Result delete(@PathVariable String userId){
         userservice.delete(userId);
        return  Result.success();
    }
    @PostMapping("/updata_shuju")
    public Result updataShuju(@RequestBody List<SeatReservation> seatReservations){
        userservice.updatashuju(seatReservations);
        return  Result.success();
    }
    @GetMapping("/blacklist/{user_id}")
    boolean getblacklist(@PathVariable("user_id") String userId){
        if(redisService.get(userId)!=null){
            return true;
        }
        return false;
    }


}
