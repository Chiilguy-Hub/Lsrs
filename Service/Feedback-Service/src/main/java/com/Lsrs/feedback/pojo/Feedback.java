package com.Lsrs.feedback.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("feedbacks")
public class Feedback {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 楼层
     */
    @TableField("seat_floor")
    private String seatFloor;
    /**
     * 学号
     */
    @TableField("user_id")
    private String userId;
    /**
     * 房间号
     */
    @TableField("room_number")
    private String roomNumber;

    /**
     * 座位号
     */
    @TableField("seat_number")
    private String seatNumber;
    /**
     * 反馈内容
     */
    private String content;
    /**
     * 图片路径
     */
    private String image;

    /**
     * 建议类型
     */
    private Integer type;
    /**
     * 处理状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}