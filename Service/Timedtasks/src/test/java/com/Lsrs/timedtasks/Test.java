package com.Lsrs.timedtasks;

import com.Lsrs.timedtasks.feign.ReservationFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class Test {
    @Autowired
    ReservationFeign reservationFeign;
    @org.junit.jupiter.api.Test 
    public void test() {
        reservationFeign.loadRedis();
    }
}
