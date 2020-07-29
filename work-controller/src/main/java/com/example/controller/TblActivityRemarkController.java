package com.example.controller;

import com.example.exception.ResultException;
import com.example.pojo.TblActivityRemark;
import com.example.pojo.TblUser;
import com.example.service.TblActivityRemarkService;
import com.example.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("activityremark")
public class TblActivityRemarkController {
    @Autowired
    private TblActivityRemarkService activityRemarkService;

    @Value("${session.user}")
    private String USER;

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Result add(@RequestBody TblActivityRemark remark, HttpServletRequest request){
        //添加创建者ID
        TblUser user = (TblUser) request.getSession().getAttribute(USER);
        remark.setCreateby(user.getId());
        try{
            activityRemarkService.add(remark);
            return Result.success();
        }catch (ResultException e){
            return Result.fail(e);
        }
    }

    @RequestMapping("list")
    public Result list(String id){
       try{
           List<TblActivityRemark> remarks = activityRemarkService.list(id);
           return Result.success(remarks);
       }catch (ResultException e){
           return Result.fail(e);
       }
    }

    @RequestMapping("update")
    public Result update(@RequestBody TblActivityRemark editMk, HttpServletRequest request){
        //添加修改者ID
        TblUser user = (TblUser) request.getSession().getAttribute(USER);
        editMk.setEditby(user.getId());
        //修改
        try{
            activityRemarkService.update(editMk);
            return Result.success();
        }catch (ResultException e){
            return Result.fail(e);
        }
    }

    @RequestMapping("/delete")
    public Result delete(String id){
       try{
           activityRemarkService.delete(id);
           return Result.success();
       }catch (ResultException e){
           return Result.fail(e);
       }
    }
}
