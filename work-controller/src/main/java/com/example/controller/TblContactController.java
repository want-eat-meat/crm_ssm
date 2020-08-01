package com.example.controller;

import com.example.pojo.TblContacts;
import com.example.pojo.TblCustomer;
import com.example.pojo.TblDicValue;
import com.example.pojo.TblUser;
import com.example.service.TblContactService;
import com.example.service.TblCustomerService;
import com.example.service.TblUserService;
import com.example.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
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
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Result add(@RequestBody TblContacts contact, HttpServletRequest request){
        //添加创建者id
        TblUser user = (TblUser) request.getSession().getAttribute(USER);
        contact.setCreateby(user.getId());
        //添加
        contactService.add(contact);
        return Result.success();
    }

    @RequestMapping("listCustomer")
    public Result listCustomers(String name){
        List<TblCustomer> customers = customerService.list(name);
        return Result.success(customers);
    }

    @RequestMapping(value = "list", method = RequestMethod.POST)
    public Result listAll(
            @RequestParam(value="start", required = true, defaultValue = "1") int start,
            @RequestParam(value="count", required = true, defaultValue = "5") int count,
            @RequestParam(value="fullname", required = false)String fullname,
            @RequestParam(value="owner", required = false)String owner,
            @RequestParam(value="phone", required = false)String phone,
            @RequestParam(value="ctmname", required = false)String ctmname,
            @RequestParam(value="source", required = false)String source,
            @RequestParam(value="birth", required = false)String birth,
            HttpServletRequest request
    ){
        HashMap<String, Set<TblDicValue>> dic = (HashMap<String, Set<TblDicValue>>) request.getSession().getServletContext().getAttribute("dic");
        PageResult pageResult = contactService.listAll(start, count, fullname, owner, phone, ctmname, source, birth);
        List<HashMap<String, Object>> rows = (List<HashMap<String, Object>>) pageResult.getRows();
        Set<TblDicValue> sources = dic.get("source");
        for(HashMap<String, Object> data: rows){
            String sourceId = ((TblContacts) data.get("contact")).getSource();
            if(sourceId != null && !"".equals(sourceId)){
                String sourceValue = "";
                for(TblDicValue value : sources){
                    if(sourceId.equals(value.getId())){
                        sourceValue = value.getValue();
                    }
                }
                data.put("sourcename", sourceValue);
            }
        }
        return Result.success(pageResult);
    }

    @RequestMapping("delete")
    public Result delete(@RequestBody List<String> ids){
        contactService.delete(ids);
        return Result.success();
    }

    @RequestMapping(value = "getById", method = RequestMethod.POST)
    public Result getById(String id, HttpServletRequest request){
        HashMap<String, Set<TblDicValue>> dics = (HashMap<String, Set<TblDicValue>>) request.getSession().getServletContext().getAttribute("dic");
        Set<TblDicValue> appels = dics.get("appellation");
        Set<TblDicValue> source = dics.get("source");
        HashMap<String, Object> contactMap = contactService.getById(id);
        TblContacts contact = (TblContacts) contactMap.get("contact");
        setDicValue(appels, contactMap, contact.getAppellation(), "appelname");
        setDicValue(source, contactMap, contact.getSource(), "sourcename");
        return Result.success(contactMap);
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Result update(@RequestBody TblContacts contacts, HttpServletRequest request){
        //添加修改者
        TblUser user = (TblUser)request.getSession().getAttribute(USER);
        contacts.setEditby(user.getId());
        //修改
        contactService.update(contacts);
        return Result.success();
    }

    private void setDicValue(Set<TblDicValue> values, HashMap<String, Object> contactMap, String target, String key) {
        if(target != null && !"".equals(target)){
            for(TblDicValue value : values){
                if(target.equals(value.getId())){
                    contactMap.put(key, value.getValue());
                }
            }
        }
    }
}
