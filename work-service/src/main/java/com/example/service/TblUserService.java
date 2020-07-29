package com.example.service;

import com.example.pojo.TblUser;

import java.util.List;

public interface TblUserService {

    TblUser login(String loginAct, String loginPsd, String ip);

    void updatePwd(String id, String oldPwd, String newPwd);

    TblUser getById(String id);

    List<TblUser> listAll();
}
