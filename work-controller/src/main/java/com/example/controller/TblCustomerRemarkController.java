package com.example.controller;

import com.example.enums.ResultEnum;
import com.example.exception.ResultException;
import com.example.pojo.*;
import com.example.service.TblContactService;
import com.example.service.TblCustomerRemarkService;
import com.example.service.TblTranService;
import com.example.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("customerRemark")
public class TblCustomerRemarkController {
    @Autowired
    private TblCustomerRemarkService remarkService;
    @Autowired
    private TblContactService contactService;
    @Autowired
    private TblTranService tranService;

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

    @RequestMapping("listTran")
    public Result listTrans(String id, HttpServletRequest request){
        List<TblTran> trans = remarkService.listTrans(id);
        //添加数据
        ServletContext servletContext = request.getSession().getServletContext();
        Map<String, String> poss = (Map<String, String>) servletContext.getAttribute("poss");
        HashMap<String, Set<TblDicValue>> dic = (HashMap<String, Set<TblDicValue>>) servletContext.getAttribute("dic");
        Set<TblDicValue> stages = dic.get("stage");
        Set<TblDicValue> types = dic.get("transactionType");
        for(TblTran tran : trans){
            if(tran.getStage() != null && !"".equals(tran.getStage())){
                tran.setPossible(poss.get(tran.getStage()));
                for(TblDicValue value : stages){
                    if(value.getId().equals(tran.getStage())){
                        tran.setStage(value.getValue());
                        break;
                    }
                }
            }
            if(tran.getType() != null && !"".equals(tran.getType())){
                for(TblDicValue value : types){
                    if(value.getId().equals(tran.getType())){
                        tran.setType(value.getValue());
                        break;
                    }
                }
            }
        }
        return Result.success(trans);
    }

    @RequestMapping("deleteTran")
    public Result deleteTran(String id){
        List<String> ids = new ArrayList<>();
        ids.add(id);
        tranService.delete(ids);
        return Result.success();
    }
}
