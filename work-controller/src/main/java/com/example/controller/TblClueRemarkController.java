package com.example.controller;

import com.example.exception.ResultException;
import com.example.pojo.TblActivity;
import com.example.pojo.TblClueActivityRelation;
import com.example.pojo.TblClueRemark;
import com.example.pojo.TblUser;
import com.example.service.TblClueRemarkService;
import com.example.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.sound.midi.Soundbank;
import java.util.List;

@RestController
@RequestMapping("clueRemark")
public class TblClueRemarkController {

    @Autowired
    private TblClueRemarkService clueRemarkService;
    @Value("${session.user}")
    private String USER;

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Result add(@RequestBody TblClueRemark addRemark, HttpServletRequest request){
        //添加创建者
        TblUser user = (TblUser)request.getSession().getAttribute(USER);
        addRemark.setCreateby(user.getId());
        //添加
        try{
            clueRemarkService.add(addRemark);
            return Result.success();
        }catch (ResultException e){
            return Result.fail(e);
        }
    }

    @RequestMapping(value = "list", method = RequestMethod.POST)
    public Result listAll(String id){
        try{
            List<TblClueRemark> remarks = clueRemarkService.list(id);
            return Result.success(remarks);
        }catch (ResultException e){
            return Result.fail(e);
        }
    }
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Result update(@RequestBody TblClueRemark editRemark, HttpServletRequest request){
        //添加修改者
        TblUser user = (TblUser) request.getSession().getAttribute(USER);
        editRemark.setEditby(user.getId());
        //修改
        try{
            clueRemarkService.update(editRemark);
            return Result.success();
        }catch (ResultException e){
            return Result.fail(e);
        }
    }
    @RequestMapping("delete")
    public Result delete(String id){
        try{
            clueRemarkService.delete(id);
            return Result.success();
        }catch (ResultException e){
            return Result.fail(e);
        }
    }

    @RequestMapping(value = "listAct", method = RequestMethod.POST)
    public Result listAct(@RequestParam("data") String data,
                          @RequestParam("ids") List<String> ids){
        try{
            List<TblActivity> activities = clueRemarkService.listAct(data, ids);
            return Result.success(activities);
        }catch (ResultException e){
            return Result.fail(e);
        }
    }

    @RequestMapping(value = "addRelation", method = RequestMethod.POST)
    public Result addRelation(@RequestParam("acts") List<String> acts,
                              @RequestParam("clueId") String clueId){
        try{
            clueRemarkService.addRelation(acts, clueId);
            return Result.success();
        }catch (ResultException e){
            return Result.fail(e);
        }
    }

    @RequestMapping(value = "listRelation", method = RequestMethod.POST)
    public Result listRelatingByClueId(String id){
        try{
            List<TblActivity> relations = clueRemarkService.listRelations(id);
            return Result.success(relations);
        }catch (ResultException e){
            return Result.fail(e);
        }
    }

    @RequestMapping(value = "deleteRelation", method = RequestMethod.POST)
    public Result deleteRelation(String actId, String clueId){
        try{
            clueRemarkService.deleteRelation(actId, clueId);
            return Result.success();
        }catch (ResultException e){
            return Result.fail(e);
        }
    }
}
