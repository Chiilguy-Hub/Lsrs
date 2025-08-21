package com.Lsrs.reservation.wx;

import com.Lsrs.reservation.feign.ResourceFeignClient;
import com.Lsrs.reservation.pojo.MessageUtils;
import com.Lsrs.reservation.config.GetHttpSessionConfigurator;
import com.Lsrs.reservation.pojo.SeatVO;
import com.Lsrs.reservation.service.Reservationservice;
import com.example.pojo.Result;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/api/resource/seat",configurator = GetHttpSessionConfigurator.class)
@Component
@Slf4j
public class ChatEndpoint {
   private static final Map<String, Session> onLineUsers = new ConcurrentHashMap<>();
   private HttpSession session;
   /*
    *建立websocket连接后，被调用
    *@param session
    */
   private static Reservationservice reservationservice;
    private static ResourceFeignClient resourceFeignClient;
    @Autowired
    public void setReservationservice(Reservationservice reservationservice) {
        ChatEndpoint.reservationservice = reservationservice;
    }

    @Autowired
    public void setResourceFeignClient(ResourceFeignClient resourceFeignClient) {
        ChatEndpoint.resourceFeignClient = resourceFeignClient;
    }
    @OnOpen
    public void open(Session session, EndpointConfig config) {
       // 手动注入依赖

       String user = (String) config.getUserProperties().get("user");
       log.info("user:"+user+"用户连入");
       onLineUsers.put(user, session);
    }
    private void broadcastAll(String zoneNumber,String zone,LocalDateTime startTime,LocalDateTime endTime) {
       try {
           //遍历map集合
           Set<Map.Entry<String, Session>> entries = onLineUsers.entrySet();
           for (Map.Entry<String, Session> entry : entries) {
               //发送消息
               Session user = entry.getValue();
               String message = MessageUtils.getMessage(getseat(zoneNumber, zone, startTime, endTime));
               user.getBasicRemote().sendText(message);
           }
       }catch (IOException e){
           //记录日志
         log.error(e.getMessage(),e);
       }
    }
    private Result getseat(String zoneNumber, String zone, LocalDateTime startTime, LocalDateTime endTime) {
        //获取所有的位置
        List <String> allSeats = resourceFeignClient.getAllRoom(Integer.parseInt(zoneNumber), zone);
        System.out.println(allSeats+"所有");
        //获取已经预约的座位
        List <String> getseats = reservationservice.getseats(Integer.parseInt(zoneNumber), startTime, endTime,zone);
        System.out.println(getseats+"已预约");
        // 转换已预约列表为集合
        Set<String> reservedSet = new HashSet<>(getseats);
        // 生成状态表
        List<SeatVO> statusList = new ArrayList<>();
        for (String seatId : allSeats) {
            boolean occupied = !reservedSet.contains(seatId);
            statusList.add(new SeatVO(seatId, occupied));
        }
        return  Result.success(statusList);
    }
/*pattern = "yyyy/MM/dd HH:mm:ss"*/
    @OnMessage
    public void message(Session session,  String message) throws IOException {
        System.out.println(message);

    }
    @OnClose
    public void close(Session session) {
        String user=(String) this.session.getAttribute("user");
     //将用户冲map集合中退出
        onLineUsers.remove(user);
    }

}
