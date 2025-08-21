package com.Lsrs.reservation.pojo;

import com.alibaba.fastjson.JSON;
import com.example.pojo.Result;

public class MessageUtils {
    public  static String getMessage(Object  message){
        return JSON.toJSONString(Result.success(message));
    }
}
