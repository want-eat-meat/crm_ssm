package com.example.service;

import com.github.pagehelper.PageInfo;

import java.util.List;

public interface TblTranService {
    PageInfo list(int start, int count, String owner, String name, String ctmname, String stage, String type, String source, String ctname);

    void delete(List<String> ids);
}
