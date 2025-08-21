package com.Lsrs.feedback.service;

import com.Lsrs.feedback.VO.FeedbackVO;
import com.Lsrs.feedback.pojo.Feedback;
import com.example.pojo.PageResult;

public interface Feedbackservice {
    void update(Feedback feedback);

    PageResult<FeedbackVO> getfeedback(Integer page, Integer limit);

    void updatestatus(Integer id, Integer status);
}
