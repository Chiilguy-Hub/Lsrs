package com.Lsrs.feedback.VO;

import lombok.Data;

@Data
public class FeedbackVO {
    private Integer id;
    private String seatFloor;
    private String roomNumber;
    private String seatNumber;
    private String content;
    private String image;
    private Integer type;
    private Integer status;
}
