package com.example.service;

import com.example.pojo.TblActivity;
import com.example.pojo.TblClueRemark;

import java.util.List;

public interface TblClueRemarkService {
    void add(TblClueRemark remark);

    List<TblClueRemark> list(String id);

    void update(TblClueRemark remark);

    void delete(String id);
    

    void addRelation(List<String> acts, String clueId);

    List<TblActivity> listRelations(String id);

    List<TblActivity> listAct(String data, List<String> ids);

    void deleteRelation(String actId, String clueId);
}
