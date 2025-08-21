package com.Lsrs.reservation.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Echartsdata {
    private List<String> x;
    private List<StudyRoomData> y;
}
