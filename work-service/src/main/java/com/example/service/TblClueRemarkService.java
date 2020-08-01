package com.example.service;

import com.example.pojo.TblActivity;
import com.example.pojo.TblClueRemark;
import com.example.utils.PageResult;

import java.util.List;

public interface TblClueRemarkService {
    void add(TblClueRemark remark);

    List<TblClueRemark> list(String id);

    void update(TblClueRemark remark);

    void delete(String id);
    

    void addRelation(List<String> acts, String clueId);

    List<TblActivity> listRelations(String id);

    void deleteRelation(String actId, String clueId);

    PageResult listActsBySearch(int start, int count, String id, String search);
}
