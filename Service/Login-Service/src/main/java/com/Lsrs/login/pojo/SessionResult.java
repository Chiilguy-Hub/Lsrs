package com.Lsrs.login.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionResult {
    private String openid;
    private String session_key; // JSON 字段是 session_key，需保持一致
    private String unionid;
    private Integer errcode;
    private String errmsg;


}