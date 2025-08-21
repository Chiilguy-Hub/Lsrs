package com.Lsrs.reservation.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudyRoomData {
    private String name;
    private List<Integer> data;
}
