package com.example.controller;

import com.example.pojo.TblContacts;
import com.example.pojo.TblCustomer;
import com.example.pojo.TblDicValue;
import com.example.pojo.TblUser;
import com.example.service.TblContactService;
import com.example.service.TblCustomerService;
import com.example.service.TblUserService;
import com.example.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.example.exception.ResultException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("contact")
public class TblContactController {
    @Autowired
    private TblContactService contactService;
    @Autowired
    private TblUserService userService;
    @Autowired
    private TblCustomerService customerService;
    @Value("${session.user}")
    private String USER;

    @RequestMapping("listMsg")
    public Result listMsg(HttpServletRequest request){
        HashMap<String, Object> resultMap = new HashMap<>();
        ServletContext servletContext = request.getSession().getServletContext();
        HashMap<String, Set<TblDicValue>> dics= (HashMap<String, Set<TblDicValue>>) servletContext.getAttribute("dic");
        try {
            //用户集合
            List<TblUser> users = userService.listAll();
            resultMap.put("users", users);
            //来源集合
            Set<TblDicValue> sources = dics.get("source");
            resultMap.put("sources", sources);
            //称呼集合
            Set<TblDicValue> appels = dics.get("appellation");
            resultMap.put("appels", appels);
            return Result.success(resultMap);
        }catch (ResultException e){
            return Result.fail(e);
        }
    }
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Result add(@RequestBody TblContacts contact, HttpServletRequest request){
        //添加创建者id
        TblUser user = (TblUser) request.getSession().getAttribute(USER);
        contact.setCreateby(user.getId());
        //添加
        try{
            contactService.add(contact);
            return Result.success();
        }catch (ResultException e){
            return Result.fail(e);
        }
    }

    @RequestMapping("listCustomer")
    public Result listCustomers(String name){
        try{
            List<TblCustomer> customers = customerService.list(name);
            return Result.success(customers);
        }catch (ResultException e){
            return Result.fail(e);
        }

    }
}
