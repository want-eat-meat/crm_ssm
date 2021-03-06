package com.example.controller;

import com.example.exception.ResultException;
import com.example.pojo.TblCustomer;
import com.example.pojo.TblUser;
import com.example.service.TblCustomerService;
import com.example.service.TblUserService;
import com.example.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.sound.midi.Soundbank;
import java.util.List;

@RestController
@RequestMapping("customer")
public class TblCustomerController {
    @Autowired
    private TblCustomerService customerService;
    @Autowired
    private TblUserService userService;

    @Value("${session.user}")
    private String USER;

    @RequestMapping("listUsers")
    public Result listUsers(){
        List<TblUser> users = userService.listAll();
        return Result.success(users);
    }
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Result add(@RequestBody TblCustomer addCustomer, HttpServletRequest request){
        //添加创建者
        TblUser user = (TblUser)request.getSession().getAttribute(USER);
        addCustomer.setCreateby(user.getId());
        //添加
        customerService.add(addCustomer);
        return Result.success();
    }

    @RequestMapping("list")
    public Result list(
            @RequestParam(value = "start",required = false) int start,
            @RequestParam(value = "count",required = false) int count,
            @RequestParam(value = "name",required = false) String name,
            @RequestParam(value = "owner",required = false) String owner,
            @RequestParam(value = "phone",required = false) String phone,
            @RequestParam(value = "website",required = false) String website
    ){
        PageResult pageResult = customerService.list(start, count, name, owner, phone, website);
        return Result.success(pageResult);
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Result delete(@RequestBody List<String> ids){
        customerService.delete(ids);
        return Result.success(ids);
    }
    @RequestMapping("edit")
    public Result getCustomerById(String id){
        TblCustomer customer = customerService.getById(id);
        return Result.success(customer);
    }
    @RequestMapping("update")
    public Result update(@RequestBody TblCustomer editCustomer, HttpServletRequest request){
        //添加编辑者id
        TblUser user = (TblUser) request.getSession().getAttribute(USER);
        editCustomer.setEditby(user.getId());
        //修改
        customerService.update(editCustomer);
        return Result.success();
    }
}
