package com.Lsrs.notice.Mapper;

import com.Lsrs.notice.pojo.UserNotification;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface NoticeMapper extends BaseMapper<UserNotification> {
       @Select("select *from sys_user_notice.user_notification where user_id=#{userId} order by created_at desc")
         List<UserNotification> selectByuserId(String userId);
}
