package com.example.controller;

import com.example.utils.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.TreeMap;

@RestController
@RequestMapping("dic")
public class TblDicController {
    @RequestMapping("list")
    public Result list(String name, HttpServletRequest request){
        ServletContext servletContext = request.getSession().getServletContext();
        HashMap<String, TreeMap<String, String>> dicMap = (HashMap<String, TreeMap<String, String>>) servletContext.getAttribute("dic");
        TreeMap<String, String> resultMap = dicMap.get(name);

        return Result.success(resultMap);
    }
}
