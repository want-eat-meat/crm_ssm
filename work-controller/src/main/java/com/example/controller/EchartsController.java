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
        return Result.success(result);
    }

    @RequestMapping("tran")
    public Result tran(HttpServletRequest request){
        Map<String, Object> result = echartsService.tran();
        ServletContext servletContext = request.getSession().getServletContext();
        Map<String, Set<TblDicValue>> dic = (Map<String, Set<TblDicValue>>) servletContext.getAttribute("dic");
        Set<TblDicValue> stages = dic.get("stage");
        List<Map<String, String>> list = (List<Map<String, String>>) result.get("tran");
        for(Map<String, String> map : list){
            String key = map.get("name");
            for(TblDicValue value : stages){
                if(value.getId().equals(key)) {
                    map.replace("name", value.getText().substring(2));
                    break;
                }
            }
        }
        List<String> stage = new ArrayList<>();
        for(TblDicValue value : stages) {
            stage.add(value.getText().substring(2));
        }
        result.put("stage", stage);
        return Result.success(result);
    }

    @RequestMapping("clue")
    public Result clue(HttpServletRequest request){
        Map<String, Object> maps = echartsService.clue();

        Map<String, Set<TblDicValue>> dic = (Map<String, Set<TblDicValue>>) request.getSession().getServletContext().getAttribute("dic");
        Set<TblDicValue> states = dic.get("clueState");
        List<String> key = new ArrayList<>();
        List<Object> value = new ArrayList<>();
        for(TblDicValue dicValue : states){
            key.add(dicValue.getText());
            Object val = 0;
            if(maps.containsKey(dicValue.getId())){
                val = maps.get(dicValue.getId());
            }
            value.add(val);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("key", key);
        result.put("value", value);
        return Result.success(result);
    }
}
