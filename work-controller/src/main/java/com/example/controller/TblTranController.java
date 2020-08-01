package com.example.controller;

import com.example.pojo.TblDicValue;
import com.example.pojo.TblUser;
import com.example.service.TblTranService;
import com.example.service.TblUserService;
import com.example.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("tran")
public class TblTranController {

    @Autowired
    private TblTranService tranService;
    @Autowired
    private TblUserService userService;

    @Value("${session.user}")
    private String USER;

    @RequestMapping("listMessage")
    public Result listMessage(HttpServletRequest request){
        ServletContext servletContext = request.getSession().getServletContext();
        HashMap<String, Set<TblDicValue>> dic = (HashMap<String, Set<TblDicValue>>) servletContext.getAttribute("dic");

        HashMap<String, Object> result = new HashMap<String, Object>();
        //用户集合
        List<TblUser> users = userService.listAll();
        result.put("users", users);
        //阶段集合
        result.put("stages", dic.get("stage"));
        //类型集合
        result.put("transactionTypes", dic.get("transactionType"));
        //来源集合
       result.put("sources", dic.get("source"));
        return Result.success(result);
    }
}
