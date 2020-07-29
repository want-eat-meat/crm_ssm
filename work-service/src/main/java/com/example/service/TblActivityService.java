package com.example.service;

import com.example.pojo.TblActivity;
import com.example.pojo.TblUser;
import com.example.utils.PageResult;

import java.util.List;

public interface TblActivityService {
    /**
     * 获取全部用户
     * @return
     */
    List<TblUser> getUserList();

    void add(TblActivity activity);

    PageResult list(String name, String owner, String startDate, String endDate, int start, int count);

    void delete(List<String> data);

    TblActivity getById(String id);

    void update(TblActivity activity);
}
