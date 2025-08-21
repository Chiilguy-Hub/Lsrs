package com.example.pojo;

import com.baomidou.mybatisplus.annotation.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("seat_reservation") // 指定表名
public class SeatReservation {

    @TableId(value = "user_id", type = IdType.NONE) //
    private String userId;

    @TableField("seat_number")
    private String seatNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime startTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime endTime;

    @TableField("zone_number")
    private int zoneNumber;
    private int status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}