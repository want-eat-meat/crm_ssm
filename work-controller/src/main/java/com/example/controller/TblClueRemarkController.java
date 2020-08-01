package com.example.controller;

import com.example.pojo.TblActivity;
import com.example.pojo.TblClueRemark;
import com.example.pojo.TblDicValue;
import com.example.pojo.TblUser;
import com.example.service.TblActivityService;
import com.example.service.TblClueRemarkService;
import com.example.utils.PageResult;
import com.example.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("clueRemark")
public class TblClueRemarkController {

    @Autowired
    private TblClueRemarkService clueRemarkService;
    @Autowired
    private TblActivityService activityService;

    @Value("${session.user}")
    private String USER;

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Result add(@RequestBody TblClueRemark addRemark, HttpServletRequest request){
        //添加创建者
        TblUser user = (TblUser)request.getSession().getAttribute(USER);
        addRemark.setCreateby(user.getId());
        //添加
        clueRemarkService.add(addRemark);
        return Result.success();
    }

    @RequestMapping(value = "list", method = RequestMethod.POST)
    public Result listAll(String id){
        List<TblClueRemark> remarks = clueRemarkService.list(id);
        return Result.success(remarks);
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Result update(@RequestBody TblClueRemark editRemark, HttpServletRequest request){
        //添加修改者
        TblUser user = (TblUser) request.getSession().getAttribute(USER);
        editRemark.setEditby(user.getId());
        //修改
        clueRemarkService.update(editRemark);
        return Result.success();
    }
    @RequestMapping("delete")
    public Result delete(String id){
        clueRemarkService.delete(id);
        return Result.success();
    }

    @RequestMapping(value = "listAct", method = RequestMethod.POST)
    public Result listAct(@RequestParam("data") String data,
                          @RequestParam("ids") List<String> ids){
        List<TblActivity> activities = activityService.listAct(data, ids);
        return Result.success(activities);
    }

    @RequestMapping(value = "addRelation", method = RequestMethod.POST)
    public Result addRelation(@RequestParam("acts") List<String> acts,
                              @RequestParam("clueId") String clueId){
        clueRemarkService.addRelation(acts, clueId);
        return Result.success();
    }

    @RequestMapping(value = "listRelation", method = RequestMethod.POST)
    public Result listRelatingByClueId(String id){
        List<TblActivity> relations = clueRemarkService.listRelations(id);
        return Result.success(relations);
    }

    @RequestMapping(value = "deleteRelation", method = RequestMethod.POST)
    public Result deleteRelation(String actId, String clueId){
        clueRemarkService.deleteRelation(actId, clueId);
        return Result.success();
    }

    @RequestMapping("getStage")
    public Result getStages(HttpServletRequest request){
        HashMap<String, Set<TblDicValue>> dic = (HashMap<String, Set<TblDicValue>>) request.getSession().getServletContext().getAttribute("dic");
        Set<TblDicValue> stages = dic.get("stage");
        return Result.success(stages);
    }

    @RequestMapping(value = "listActsBySearch", method = RequestMethod.POST)
    public Result listActsBySearch(
             int start,
             int count,
             String id,
             @RequestParam(value="search",required = false) String search){
        PageResult pageResult = clueRemarkService.listActsBySearch(start, count, id, search);
        return Result.success(pageResult);
    }
}
