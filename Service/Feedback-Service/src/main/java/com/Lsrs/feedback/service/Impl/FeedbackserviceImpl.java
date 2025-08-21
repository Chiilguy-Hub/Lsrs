package com.Lsrs.feedback.service.Impl;

import com.Lsrs.feedback.Mapper.FeedbackMapper;
import com.Lsrs.feedback.VO.FeedbackVO;
import com.Lsrs.feedback.pojo.Feedback;
import com.Lsrs.feedback.service.Feedbackservice;
import com.example.pojo.PageResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class FeedbackserviceImpl    implements Feedbackservice {
    @Autowired
    FeedbackMapper feedbackmapper;
    @Override
    public void update(Feedback feedback) {
     feedback.setCreateTime(LocalDateTime.now());
     feedback.setUpdateTime(LocalDateTime.now());
     feedback.setType(feedback.getType()+1);
        feedbackmapper.insert(feedback);
    }

    @Override
    public PageResult<FeedbackVO> getfeedback(Integer page,Integer limit) {
        PageResult<FeedbackVO> feedbackPageResult = new PageResult<>();

        Integer count= feedbackmapper.getcount1();//获取对应的数量
        Integer limit1 = (page - 1) * limit;
        List<Feedback> list=feedbackmapper.getfeedback(limit1);

        List<FeedbackVO> listVO=new ArrayList<>();
        for (Feedback feedback : list) {
            FeedbackVO feedbackVO = new FeedbackVO();
            BeanUtils.copyProperties(feedback, feedbackVO); // 复制单个对象
            listVO.add(feedbackVO); // 添加到新集合
        }
        feedbackPageResult.setList(listVO);
        feedbackPageResult.setTotal(count);
        return feedbackPageResult;
    }

    @Override
    public void updatestatus(Integer id, Integer status) {
        feedbackmapper.updatestatus(id,status);
    }
}
