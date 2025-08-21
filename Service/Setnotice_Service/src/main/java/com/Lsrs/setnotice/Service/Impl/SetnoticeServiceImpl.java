package com.Lsrs.setnotice.Service.Impl;

import com.Lsrs.setnotice.Mapper.SetnoticeMapper;
import com.Lsrs.setnotice.Pojo.LibraryActivity;
import com.Lsrs.setnotice.Service.SetnoticeService;
import com.example.pojo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SetnoticeServiceImpl implements SetnoticeService {
    @Autowired
    private SetnoticeMapper setnoticeMapper;
    @Override
    public PageResult<LibraryActivity> getNoticeList(Integer page, Integer limit) {
        PageResult<LibraryActivity>  setnoticePageResult = new PageResult<>();

        Integer count= setnoticeMapper.getcount1();//获取对应的数量
        Integer limit1 = (page - 1) * limit;
        List<LibraryActivity> list=setnoticeMapper.getNoticeList(limit1);
        setnoticePageResult.setList(list);
        setnoticePageResult.setTotal(count);
        return setnoticePageResult;
    }

    @Override
    public void creat(LibraryActivity libraryActivity) {
        setnoticeMapper.insert(libraryActivity);
    }

    @Override
    public void update(LibraryActivity libraryActivity) {
        setnoticeMapper.updateById(libraryActivity);
    }

    @Override
    public void delete(Integer id) {
        setnoticeMapper.deleteById(id);
    }



}
