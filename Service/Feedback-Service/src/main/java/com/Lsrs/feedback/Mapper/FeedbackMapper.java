package com.Lsrs.feedback.Mapper;

import com.Lsrs.feedback.pojo.Feedback;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;


public interface FeedbackMapper extends  BaseMapper<Feedback> {
   @Select("select count(id) from sys_feedback.feedbacks")
    Integer getcount1();


@Select("select * from sys_feedback.feedbacks limit  #{limit1} ,10")
    List<Feedback> getfeedback(Integer limit1);
@Update("update sys_feedback.feedbacks set status=#{status} where id=#{id}")
    void updatestatus(Integer id, Integer status);
}
