package com.Lsrs.setnotice.Controller;

import com.Lsrs.setnotice.Pojo.LibraryActivity;
import com.Lsrs.setnotice.Service.SetnoticeService;

import com.example.pojo.PageResult;
import com.example.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/setnotice")
public class SetnoticeController {
    @Autowired
    private SetnoticeService setnoticeService;

    @GetMapping("/getNoticeList/{page}")
    public Result getNoticeList(@PathVariable Integer page, @RequestParam(defaultValue = "10") Integer limit){

        PageResult<LibraryActivity> result= setnoticeService.getNoticeList(page,limit);
        return Result.success(result);
    }
    @PostMapping("/update/{id}")
    public  Result update(@PathVariable Integer id,@RequestBody LibraryActivity libraryActivity) {

        setnoticeService.update(libraryActivity);
          return Result.success();
    }
    @PostMapping("/creat")
    public Result create(@RequestBody LibraryActivity libraryActivity) {
        System.out.println(libraryActivity);
        setnoticeService.creat(libraryActivity);
        return Result.success();
    }
    @PostMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
         setnoticeService.delete(id);
                 return Result.success();
    }

}

