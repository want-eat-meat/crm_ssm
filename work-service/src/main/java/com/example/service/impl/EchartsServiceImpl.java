package com.example.service.impl;

import com.example.mapper.TblActivityMapper;
import com.example.mapper.TblClueMapper;
import com.example.mapper.TblTranMapper;
import com.example.service.EchartsService;
import org.apache.log.output.AbstractOutputTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sound.midi.Soundbank;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EchartsServiceImpl implements EchartsService {

    @Autowired
    private TblActivityMapper activityMapper;
    @Autowired
    private TblTranMapper tranMapper;
    @Autowired
    private TblClueMapper clueMapper;


    @Override
    public Map<String, Object> activity() {
        List<Map<String, String>> actMap = activityMapper.selectCreate();
        List<String> key = new ArrayList<>();
        List<Object> value = new ArrayList<>();
        for(Map<String, String> map : actMap){
            key.add(map.get("createdate"));
            value.add(map.get("datenumber"));
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("key", key);
        resultMap.put("value", value);
        return resultMap;
    }

    @Override
    public Map<String, Object> tran() {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, String>> maps = tranMapper.tranStage();
        result.put("tran", maps);
        return result;
    }

    @Override
    public Map<String, Object> clue() {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, String>> maps = clueMapper.clueState();
        for(Map<String, String> map : maps){
            result.put(map.get("state"), map.get("number"));
        }
        return result;
    }
}
