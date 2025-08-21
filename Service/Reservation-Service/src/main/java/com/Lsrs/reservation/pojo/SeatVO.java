package com.Lsrs.reservation.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//向前端发送座位状态
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeatVO {
   private String  seatId;
   private boolean  available;
}
