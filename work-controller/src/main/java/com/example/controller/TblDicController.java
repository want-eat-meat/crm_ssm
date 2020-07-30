package com.example.controller;

import com.example.pojo.TblDicValue;
import com.example.utils.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Set;

@RestController
@RequestMapping("dic")
public class TblDicController {
    @RequestMapping("list")
    public Result list(String name, HttpServletRequest request){
        ServletContext servletContext = request.getSession().getServletContext();
        HashMap<String, Set<TblDicValue>> dicMap = (HashMap<String, Set<TblDicValue>>) servletContext.getAttribute("dic");
        Set<TblDicValue> resultSet = dicMap.get(name);
        return Result.success(resultSet);
    }
}
