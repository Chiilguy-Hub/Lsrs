package com.example.Dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Lombok 自动生成 getter/setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomCapacityDTO {
    private Integer zoneNumber; // 房间号
    private Integer count;  // 容量
}