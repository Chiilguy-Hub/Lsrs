package com.Lsrs.feedback.controller;

import com.Lsrs.feedback.VO.FeedbackVO;
import com.Lsrs.feedback.pojo.Feedback;
import com.Lsrs.feedback.service.Feedbackservice;
import com.example.pojo.PageResult;
import com.example.pojo.Result;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {
    @Autowired
    Feedbackservice feedbackservice;
    @PostMapping("/update")
        public Result update(@RequestBody Feedback feedback) {
        //获得 反馈信息表
        System.out.println(feedback);
        feedback.setStatus(0);
        feedbackservice.update(feedback);
         return Result.success();
    }
    @GetMapping("/getfeedback/{page}")
    public Result<PageResult> getresource(@PathVariable Integer page,@RequestParam(defaultValue = "10") Integer limit) {
        PageResult<FeedbackVO> getfeedback = feedbackservice.getfeedback(page, limit);
        return Result.success(getfeedback);
    }
@PostMapping("/update/{id}")
    public Result update(@PathVariable Integer id, @RequestBody Map<String, Integer> requestBody) {
Integer  status=requestBody.get("status");
    System.out.println(status);
        feedbackservice.updatestatus(id,status);
        return Result.success();

}

}
