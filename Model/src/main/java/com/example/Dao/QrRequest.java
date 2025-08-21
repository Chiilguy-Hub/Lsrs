package com.example.Dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QrRequest {
    private Integer seatFloor;
    private Integer zoneNumber;
    private String seatNumber;
    private String uuid;
    public  String userId;
}
