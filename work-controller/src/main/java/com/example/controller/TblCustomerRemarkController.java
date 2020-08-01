package com.example.controller;

import com.example.pojo.TblContacts;
import com.example.pojo.TblCustomerRemark;
import com.example.pojo.TblDicValue;
import com.example.pojo.TblUser;
import com.example.service.TblContactService;
import com.example.service.TblCustomerRemarkService;
import com.example.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("customerRemark")
public class TblCustomerRemarkController {
    @Autowired
    private TblCustomerRemarkService remarkService;
    @Autowired
    private TblContactService contactService;

    @Value("${session.user}")
    private String USER;

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Result add(@RequestBody TblCustomerRemark remark, HttpServletRequest request){
        //设置创建者
        TblUser user = (TblUser)request.getSession().getAttribute(USER);
        remark.setCreateby(user.getId());
        //添加
        remarkService.add(remark);
        return Result.success();
    }

    @RequestMapping(value = "list", method = RequestMethod.POST)
    public Result list(String id){
        List<TblCustomerRemark> remarks = remarkService.list(id);
        return Result.success(remarks);
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Result update(@RequestBody TblCustomerRemark remark, HttpServletRequest request){
        //添加编辑者id
        TblUser user = (TblUser)request.getSession().getAttribute(USER);
        remark.setEditby(user.getId());
        //修改
        remarkService.update(remark);
        return Result.success();
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Result delete(String id){
        remarkService.delete(id);
        return Result.success();
    }

    @RequestMapping("listMsg")
    public Result listMsg(HttpServletRequest request){
        HashMap<String, Set<TblDicValue>> dic = (HashMap<String, Set<TblDicValue>>) request.getSession().getServletContext().getAttribute("dic");
        HashMap<String, Set<TblDicValue>> resultMap = new HashMap<>();
        resultMap.put("sources", dic.get("source"));
        resultMap.put("appels", dic.get("appellation"));
        return Result.success(resultMap);
    }

    @RequestMapping(value = "addContact", method = RequestMethod.POST)
    public Result addContact(@RequestBody TblContacts contacts, HttpServletRequest request){
        //设置创建者
        TblUser user = (TblUser)request.getSession().getAttribute(USER);
        contacts.setCreateby(user.getId());
        //添加
        remarkService.addContact(contacts);
        return Result.success();
    }

    @RequestMapping("listContact")
    public Result listContact(String id){
        List<TblContacts> contacts = remarkService.listContact(id);
        return Result.success(contacts);
    }

    @RequestMapping("deleteContact")
    public Result deleteContact(String id){
        List<String> ids = new ArrayList<>();
        ids.add(id);
        contactService.delete(ids);
        return Result.success();
    }
}
