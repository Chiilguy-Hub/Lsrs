package com.Lsrs.resource.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data

@TableName("room_seats")
public class room_seat {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id; // 座位ID
    private int seatFloor; // 楼层
    private int zoneNumber; // 房间号
    private String seatNumber; // 座位编号（如A01/B01）
    private int type;// 座位状态


    private LocalDateTime createdAt; // 创建时间

    private LocalDateTime updatedAt; // 最后更新时间



}
