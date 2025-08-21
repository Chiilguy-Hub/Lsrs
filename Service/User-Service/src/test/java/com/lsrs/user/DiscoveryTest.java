package com.lsrs.user;

import com.Lsrs.user.Mapper.UserMapper;
import com.example.Dao.scoreDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@SpringBootTest
public class DiscoveryTest {

   @Autowired
   DiscoveryClient discoveryClient;
@Autowired
    UserMapper userMapper;

   @Test
  void  discoverClient() {

      discoveryClient.getServices().forEach(s -> {
          System.out.println(s);

          //获取 ip+port
          List<ServiceInstance> instances = discoveryClient.getInstances(s);
          instances.forEach(serviceInstance -> {
              System.out.println(serviceInstance.getHost() + ":" + serviceInstance.getPort());
          });

      });
  }
  @Test
    void  discoverClient2() {

  }
}
