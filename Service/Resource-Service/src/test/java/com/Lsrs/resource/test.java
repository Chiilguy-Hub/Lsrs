package com.Lsrs.resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.Lsrs.resource.Mapper.ResourceMapper;
import com.Lsrs.resource.pojo.room_seat;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
@MapperScan("com.Lsrs.resource.Mapper")
@SpringBootTest
public class test {
    @Autowired
    ResourceMapper resourceMapper;

    @Test
    public void test() {
        for(int i=1;i<=6;i++){
            for(int l=1;l<=3;l++){
                int x=i*100+l;
                for(int j=0;j<3;j++) {
                    char c = (char) ('A' + j);
                    for (int k = 1; k <= 20; k++) {
                        room_seat room = new room_seat();
                        if (k < 10) {
                            room.setSeatNumber(c + "0" + k);
                        } else {
                            room.setSeatNumber(c + "" + k);
                        }
                        room.setZoneNumber(x);
                        room.setCreatedAt(LocalDateTime.now());
                        room.setUpdatedAt(LocalDateTime.now());
                        room.setSeatFloor(i);
                        resourceMapper.insert(room);

                    }
                }
            }
        }



    }
    @Test
    public void test1(){
        List<Map<String, Integer>> roombyfloor = resourceMapper.getRoombyfloor("1");
        roombyfloor.forEach(room -> {
            System.out.println(room.get("zone_number"));
        });

    }

}

