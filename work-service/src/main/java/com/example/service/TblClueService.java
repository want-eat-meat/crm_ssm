package com.example.service;

import com.example.pojo.TblClue;
import com.example.pojo.TblUser;
import com.example.utils.PageResult;

import java.util.List;

public interface TblClueService {
    List<TblUser> listUsers();

    void add(TblClue clue);

    PageResult list(int start, int count, String fullname, String company, String phone, String source, String owner, String mphone, String state);

    void delete(List<String> ids);

    TblClue getById(String id);

    void update(TblClue clue);
}
