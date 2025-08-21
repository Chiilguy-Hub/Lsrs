package com.example.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class room {
    private int seatId;
    private String seatFloor;
    private int  zoneNumber;
    private String roomName;
    private String ceatedAt;
    private String updatedAt;
}