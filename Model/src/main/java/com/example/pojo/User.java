package com.example.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "library_user")

public class User {
    /*
       学号
     */
    @TableId(value = "wechat_openid")
    private String wechatOpenid;
    private String userId;
    private String userName;
    private String userImg;
    private String realName;
    private String phone;
    private String userFaculty;
    private String userMajor;
    private int totalReservations = 0;
    private int violationCount = 0;
    private LocalDateTime createdAt;
}
