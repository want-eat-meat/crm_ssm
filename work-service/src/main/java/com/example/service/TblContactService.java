package com.example.service;

import com.example.pojo.TblContacts;
import com.example.utils.PageResult;

import java.util.HashMap;
import java.util.List;

public interface TblContactService {
    void add(TblContacts contact);

    PageResult listAll(int start, int count, String fullname, String owner, String phone, String ctmname, String source, String birth);

    void delete(List<String> ids);

    HashMap<String, Object> getById(String id);

    void update(TblContacts contacts);
}
