package com.Lsrs.notice.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserNotification {
    @TableId(value = "notification_id", type = IdType.AUTO) // 自增主键
    private Integer notificationId;

    @TableField("user_id")
    private String userId;

    @TableField("zone_number")
    private Integer zoneNumber; // 注释：101 房间号

    @TableField("seat_number")
    private String seatNumber; // 注释：座位编号（如A01/B12）

    @TableField("type")
    private Integer type;

    @TableField("content")
    private String content;

    @TableField(value = "created_at", fill = FieldFill.INSERT) // 自动填充插入时间
    private LocalDateTime createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE) // 自动填充更新时间
    private LocalDateTime updatedAt;
}
