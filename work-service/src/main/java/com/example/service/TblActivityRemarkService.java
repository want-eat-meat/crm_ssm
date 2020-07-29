package com.example.service;

import com.example.pojo.TblActivityRemark;

import java.util.List;


public interface TblActivityRemarkService {
    void add(TblActivityRemark remark);

    List<TblActivityRemark> list(String id);

    void update(TblActivityRemark remark);

    void delete(String id);
}
