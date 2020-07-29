package com.example.service;

import java.util.HashMap;
import java.util.TreeMap;

public interface TblDicService {
    HashMap<String, TreeMap<String, String>> getDics();
}
