package com.example.controller;

import com.example.pojo.TblCustomerRemark;
import com.example.pojo.TblUser;
import com.example.service.TblCustomerRemarkService;
import com.example.utils.*;
import com.example.exception.ResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("customerRemark")
public class TblCustomerRemarkController {
    @Autowired
    private TblCustomerRemarkService remarkService;

    @Value("${session.user}")
    private String USER;

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Result add(@RequestBody TblCustomerRemark remark, HttpServletRequest request){
        //设置创建者
        TblUser user = (TblUser)request.getSession().getAttribute(USER);
        remark.setCreateby(user.getId());
        //添加
        try{
            remarkService.add(remark);
            return Result.success();
        }catch (ResultException e){
            return Result.fail(e);
        }
    }

    @RequestMapping(value = "list", method = RequestMethod.POST)
    public Result list(String id){
        try{
            List<TblCustomerRemark> remarks = remarkService.list(id);
            return Result.success(remarks);
        }catch (ResultException e){
            return Result.fail(e);
        }
    }
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Result update(@RequestBody TblCustomerRemark remark, HttpServletRequest request){
        //添加编辑者id
        TblUser user = (TblUser)request.getSession().getAttribute(USER);
        remark.setEditby(user.getId());
        //修改
        try{
            remarkService.update(remark);
            return Result.success();
        }catch (ResultException e){
            return Result.fail(e);
        }
    }
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Result delete(String id){
        try{
            remarkService.delete(id);
            return Result.success();
        }catch (ResultException e){
            return Result.fail(e);
        }
    }
}
