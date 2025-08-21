package com.Lsrs.setnotice.Mapper;

import com.Lsrs.setnotice.Pojo.LibraryActivity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetnoticeMapper extends BaseMapper<LibraryActivity> {
    @Select("select * from sys_notice.library_activity limit  #{limit1} ,10")
    List<LibraryActivity> getNoticeList(Integer limit1);
    @Select("select count(id) from sys_notice.library_activity")
    Integer getcount1();


}
