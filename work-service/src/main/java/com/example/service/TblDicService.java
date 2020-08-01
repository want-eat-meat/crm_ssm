package com.example.service;

import com.example.pojo.TblDicValue;

import java.util.HashMap;
import java.util.Set;

public interface TblDicService {
    HashMap<String, Set<TblDicValue>> getDics();
}
