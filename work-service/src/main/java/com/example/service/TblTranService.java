package com.example.service;

import com.example.pojo.TblDicValue;
import com.example.pojo.TblTran;
import com.example.pojo.TblTranHistory;
import com.example.pojo.TblTranRemark;
import com.example.utils.PageResult;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TblTranService {
    PageInfo list(int start, int count, String owner, String name, String ctmname, String stage, String type, String source, String ctname);

    void delete(List<String> ids);

    PageResult listActivity(int start, int count, String search);

    PageResult listContact(int start, int count, String search);

    List<Map<String, String>> listCustomer(String name);

    void add(TblTran tran);

    Map<String, Object> getById(String id);

    List<TblTranHistory> listHistory(String id);

    void addRemark(TblTranRemark remark);

    List<TblTranRemark> listRemark(String id);

    void updateRemark(TblTranRemark remark);

    void deleteRemark(String id);

    void update(TblTran tran);

    void updateStage(TblTran tran);

    List<Map<String, String>> listStage(String id, Set<TblDicValue> stages, Map<String, String> poss);
}
