package com.Lsrs.reservation.config;

import com.example.Until.JwtUntil;
import jakarta.websocket.HandshakeResponse;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpointConfig;


import java.util.List;
import java.util.Map;

public class GetHttpSessionConfigurator extends ServerEndpointConfig.Configurator {
  @Override
  public  void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request , HandshakeResponse response) {
    Map<String, List<String>> params = request.getParameterMap();
    String token = params.get("token").get(0);
    //将httpSession对象存储到配置对象中
    String username = String.valueOf(JwtUntil.parseJwt(token));
    sec.getUserProperties().put("user", username);
  }
}
