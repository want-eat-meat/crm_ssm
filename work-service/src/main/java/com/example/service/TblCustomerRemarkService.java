package com.example.service;

import com.example.pojo.TblContacts;
import com.example.pojo.TblCustomerRemark;

import java.util.List;

public interface TblCustomerRemarkService {
    void add(TblCustomerRemark remark);

    List<TblCustomerRemark> list(String id);

    void update(TblCustomerRemark remark);

    void delete(String id);

    void addContact(TblContacts contact);

    List<TblContacts> listContact(String id);
}
