package com.example.service.impl;

import com.example.mapper.TblDicTypeMapper;
import com.example.mapper.TblDicValueMapper;
import com.example.pojo.TblDicType;
import com.example.pojo.TblDicValue;
import com.example.pojo.TblDicValueExample;
import com.example.service.TblDicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("dic")
public class TblDicServiceImpl implements TblDicService {

    @Autowired
    TblDicTypeMapper typeMapper;
    @Autowired
    TblDicValueMapper valueMapper;
    @Override
    public HashMap<String, TreeMap<String, String>> getDics() {
        List<TblDicType> dicTypes = typeMapper.selectByExample(null);
        HashMap<String, TreeMap<String, String>> dicMap = new HashMap<>();
        for(TblDicType dicType : dicTypes){
            TblDicValueExample valueExample = new TblDicValueExample();
            valueExample.createCriteria().andTypecodeEqualTo(dicType.getCode());
            List<TblDicValue> dicValues = valueMapper.selectByExample(valueExample);
            TreeMap<String, String> valueMap = new TreeMap<>();
            for(TblDicValue dicValue : dicValues){
                valueMap.put(dicValue.getOrderno(), dicValue.getValue());
            }
            dicMap.put(dicType.getCode(), valueMap);
        }
        return dicMap;
    }
}
