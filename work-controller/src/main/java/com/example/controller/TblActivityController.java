package com.example.controller;

import com.example.exception.ResultException;
import com.example.pojo.TblActivity;
import com.example.pojo.TblUser;
import com.example.service.TblActivityService;
import com.example.utils.PageResult;
import com.example.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("activity")
public class TblActivityController {
    @Autowired
    TblActivityService activityService;
    @Value("${session.user}")
    private String USER;

    @RequestMapping("listUser")
    public Result getUserList(){
        try{
            List<TblUser> users = activityService.getUserList();
            return Result.success(users);
        }catch (ResultException e){
            return Result.fail(e);
        }
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Result add(@RequestBody TblActivity activity, HttpServletRequest request){
        //插入创建人ID
        TblUser user = (TblUser)request.getSession().getAttribute(USER);
        activity.setCreateby(user.getId());
        //调用service
        try{
            activityService.add(activity);
            return Result.success();
        }catch (ResultException e){
            return Result.fail(e);
        }
    }

    @RequestMapping(value = "list", method = RequestMethod.POST)
    public Result list(@RequestParam(value = "name", required = false) String name,
                       @RequestParam(value = "owner", required = false)String owner,
                       @RequestParam(value = "startDate", required = false) String startDate,
                       @RequestParam(value = "endDate", required = false)String endDate,
                       @RequestParam(value = "start", required = true, defaultValue = "1")int start,
                       @RequestParam(value = "count", required = true, defaultValue = "5") int count) {
        try{
            PageResult pageResult = activityService.list(name, owner, startDate, endDate, start, count);
            return Result.success(pageResult);
        }catch (ResultException e){
            return Result.fail(e);
        }
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Result delete(@RequestBody List<String> data){
        try{
            activityService.delete(data);
            return Result.success();
        }catch (ResultException e){
            return Result.fail(e);
        }
    }
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public Result edit(String id){
        try{
            TblActivity activity = activityService.getById(id);
            return Result.success(activity);
        }catch (ResultException e){
            return Result.fail(e);
        }
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Result update(@RequestBody TblActivity checkAct, HttpServletRequest request){
        TblUser user = (TblUser) request.getSession().getAttribute(USER);
        checkAct.setEditby(user.getId());
        try{
            activityService.update(checkAct);
            return Result.success();
        }catch (ResultException e){
            return Result.fail(e);
        }
    }
}
