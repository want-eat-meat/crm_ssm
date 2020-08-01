package com.example.service.impl;

import com.example.mapper.TblDicTypeMapper;
import com.example.mapper.TblDicValueMapper;
import com.example.pojo.TblDicType;
import com.example.pojo.TblDicValue;
import com.example.pojo.TblDicValueExample;
import com.example.comparator.DicValueComparator;
import com.example.service.TblDicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class TblDicServiceImpl implements TblDicService {

    @Autowired
    TblDicTypeMapper typeMapper;
    @Autowired
    TblDicValueMapper valueMapper;
    @Override
    public HashMap<String, Set<TblDicValue>> getDics() {
        List<TblDicType> dicTypes = typeMapper.selectByExample(null);
        HashMap<String, Set<TblDicValue>> dicMap = new HashMap<>();
        for(TblDicType dicType : dicTypes){
            TblDicValueExample valueExample = new TblDicValueExample();
            valueExample.createCriteria().andTypecodeEqualTo(dicType.getCode());
            List<TblDicValue> dicValues = valueMapper.selectByExample(valueExample);
            Set<TblDicValue> dicValueSet = new TreeSet<>(new DicValueComparator());
            dicValueSet.addAll(dicValues);
            dicMap.put(dicType.getCode(), dicValueSet);
        }
        return dicMap;
    }
}
