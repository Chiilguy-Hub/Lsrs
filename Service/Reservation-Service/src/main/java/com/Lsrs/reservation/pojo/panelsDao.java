package com.Lsrs.reservation.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class panelsDao {
    private String title;
    private Integer value;
    private String  unit;
    private String  unitColor;
    private String  subTitle;
    private String subValue;
    private String  subUnit;
}
