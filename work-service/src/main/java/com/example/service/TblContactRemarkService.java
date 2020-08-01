package com.example.service;

import com.example.pojo.TblActivity;
import com.example.pojo.TblContactsRemark;

import java.util.List;

public interface TblContactRemarkService {

    void add(TblContactsRemark remark);

    List<TblContactsRemark> list(String id);

    void update(TblContactsRemark remark);

    void delete(String id);

    void addRelation(List<String> ids, String id);

    List<TblActivity> listRelations(String id);

    void deleteRelation(String actId, String ctId);
}
