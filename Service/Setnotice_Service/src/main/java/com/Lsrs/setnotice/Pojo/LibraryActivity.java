package com.Lsrs.setnotice.Pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@TableName("library_activity")
@AllArgsConstructor
@NoArgsConstructor
public class LibraryActivity {
    @TableId(value = "id", type = IdType.AUTO)

    private Integer id;

    @TableField("activity_title")
    private String activityTitle;


    @TableField("activity_desc")

    private String activityDesc;

    @TableField("cover_image_url")

    private String coverImageUrl;

    @TableField("start_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")

    private LocalDateTime startTime;

    @TableField("end_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")

    private LocalDateTime endTime;
    
    @TableField("location_zone_id")

    private String locationZoneId;

    @TableField("status")
    private Integer status;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;


}