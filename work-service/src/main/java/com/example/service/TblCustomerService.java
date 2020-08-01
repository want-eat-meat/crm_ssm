package com.example.service;

import com.example.pojo.TblCustomer;
import com.example.utils.PageResult;

import java.util.List;

public interface TblCustomerService {
    void add(TblCustomer customer);

    PageResult list(int start, int count, String name, String owner, String phone, String website);

    void delete(List<String> ids);

    TblCustomer getById(String id);

    void update(TblCustomer customer);

    List<TblCustomer> list(String name);
}
