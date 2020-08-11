package com.example.controller;

import com.example.pojo.TblDicValue;
import com.example.service.EchartsService;
import com.example.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("echarts")
public class EchartsController {

    @Autowired
    private EchartsService echartsService;

    @RequestMapping("activity")
    public Result activity(){
        Map<String, Object> result = echartsService.activity();
        List<String> key = (List<String>) result.get("key");
        List<Object> value = (List<Object>) result.get("value");
        List<Map<String, Object>> mapList = new ArrayList<>();
        long size = 0;
        for(int i = 0; i < key.size(); i++){
            Map<String, Object> map = new HashMap<>();
            map.put("name", key.get(i));
            map.put("value", value.get(i));
            mapList.add(map);
            size += (long)value.get(i);
        }
        result.put("activity", mapList);
        result.put("size", size);
        return Result.success(result);
    }

    @RequestMapping("tran")
    public Result tran(HttpServletRequest request){
        Map<String, Object> result = echartsService.tran();
        ServletContext servletContext = request.getSession().getServletContext();
        Map<String, Set<TblDicValue>> dic = (Map<String, Set<TblDicValue>>) servletContext.getAttribute("dic");
        Set<TblDicValue> stages = dic.get("stage");
        List<Map<String, Object>> list = (List<Map<String, Object>>) result.get("tran");
        List<String> stage = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        long size = 0;
        for(Map<String, Object> map : list){
            String key = (String) map.get("name");
            values.add(map.get("value"));
            size += (long)map.get("value");
            for(TblDicValue value : stages){
                if(value.getId().equals(key)) {
                    map.replace("name", value.getText().substring(2));
                    stage.add(value.getText().substring(2));
                    break;
                }
            }
        }
        result.put("stage", stage);
        result.put("value", values);
        result.put("size", size);
        return Result.success(result);
    }

    @RequestMapping("clue")
    public Result clue(HttpServletRequest request){
        Map<String, Object> maps = echartsService.clue();

        Map<String, Set<TblDicValue>> dic = (Map<String, Set<TblDicValue>>) request.getSession().getServletContext().getAttribute("dic");
        Set<TblDicValue> states = dic.get("clueState");
        List<String> key = new ArrayList<>();
        List<Object> value = new ArrayList<>();
        List<Map<String, Object>> clue = new ArrayList<>();
        long max = 0;
        for(TblDicValue dicValue : states){
            key.add(dicValue.getText());
            Object val = 0;
            if(maps.containsKey(dicValue.getId())){
                Map<String, Object> map= new HashMap<>();
                val = maps.get(dicValue.getId());
                map.put("name", dicValue.getText());
                map.put("value", val);
                clue.add(map);
                long number = (long) maps.get(dicValue.getId());
                max += number;
            }
            value.add(val);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("key", key);
        result.put("value", value);
        result.put("clue", clue);
        result.put("max", max);
        return Result.success(result);
    }
}
