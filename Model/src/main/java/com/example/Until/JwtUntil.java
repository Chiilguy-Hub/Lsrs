package com.example.Until;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;


/**
 * 生成和解析令牌 
 */
public class JwtUntil {
    private static final String admin = "Lsrs";
    private static final long time = 3600 * 1000*2; // 1小时

    /**
     * 创建令牌
     * @param claim
     * @return
     */
    public static String creatJwt(Map<String,Object> claim){
        String jwt = Jwts.builder()
                .setHeaderParam("typ","JWT")   // 类型
                .setHeaderParam("alg","HS256") // 模式
                .addClaims(claim)
                .signWith(SignatureAlgorithm.HS256,admin) // 密钥
                .setExpiration(new Date(System.currentTimeMillis() + time)) // 有效时间
                .compact(); // 转成对象
        return jwt;
    }

    /**
     * 解析效验JWT令牌
     * @param jwt
     * @return
     */
    public static Claims parseJwt(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(admin) // 效验 admin
                .parseClaimsJws(jwt)// 解析 令牌
                .getBody();
        return claims;
    }
}