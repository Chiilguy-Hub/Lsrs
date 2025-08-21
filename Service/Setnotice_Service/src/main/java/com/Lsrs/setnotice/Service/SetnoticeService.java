package com.Lsrs.setnotice.Service;

import com.Lsrs.setnotice.Pojo.LibraryActivity;
import com.example.pojo.PageResult;

public interface SetnoticeService {
    PageResult<LibraryActivity> getNoticeList(Integer page, Integer limit);

    void creat(LibraryActivity libraryActivity);

    void update(LibraryActivity libraryActivity);

    void delete(Integer id);


}
