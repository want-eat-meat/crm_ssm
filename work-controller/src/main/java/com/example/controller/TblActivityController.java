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
        List<TblUser> users = activityService.getUserList();
        return Result.success(users);
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Result add(@RequestBody TblActivity activity, HttpServletRequest request){
        //插入创建人ID
        TblUser user = (TblUser)request.getSession().getAttribute(USER);
        activity.setCreateby(user.getId());
        //调用service
        activityService.add(activity);
        return Result.success();
    }

    @RequestMapping(value = "list", method = RequestMethod.POST)
    public Result list(@RequestParam(value = "name", required = false) String name,
                       @RequestParam(value = "owner", required = false)String owner,
                       @RequestParam(value = "startDate", required = false) String startDate,
                       @RequestParam(value = "endDate", required = false)String endDate,
                       @RequestParam(value = "start", required = true, defaultValue = "1")int start,
                       @RequestParam(value = "count", required = true, defaultValue = "5") int count) {
        PageResult pageResult = activityService.list(name, owner, startDate, endDate, start, count);
        return Result.success(pageResult);
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Result delete(@RequestBody List<String> data){
        activityService.delete(data);
        return Result.success();
    }
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public Result edit(String id){
        TblActivity activity = activityService.getById(id);
        return Result.success(activity);
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Result update(@RequestBody TblActivity checkAct, HttpServletRequest request){
        TblUser user = (TblUser) request.getSession().getAttribute(USER);
        checkAct.setEditby(user.getId());
        activityService.update(checkAct);
        return Result.success();
    }
}
