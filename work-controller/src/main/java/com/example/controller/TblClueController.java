package com.example.controller;

import com.example.pojo.TblClue;
import com.example.pojo.TblUser;
import com.example.service.TblClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import com.example.utils.*;
import com.example.exception.ResultException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("clue")
public class TblClueController {

    @Autowired
    private TblClueService clueService;
    @Value("${session.user}")
    private String USER;

    @RequestMapping("listUser")
    public Result getAllUser(){
        try{
            List<TblUser> users = clueService.listUsers();
            return Result.success(users);
        }catch (ResultException e){
            return Result.fail(e);
        }
    }
    @RequestMapping("add")
    public Result add(@RequestBody TblClue addClue, HttpServletRequest request){
        //设置创建者id
        TblUser user = (TblUser) request.getSession().getAttribute(USER);
        addClue.setCreateby(user.getId());
        //添加
        try{
            clueService.add(addClue);
            return Result.success();
        }catch (ResultException e){
            return Result.fail(e);
        }
    }

    @RequestMapping(value = "/listAll", method = RequestMethod.POST)
    public Result listAll(@RequestParam(value = "start", required = true, defaultValue = "1")int start,
                          @RequestParam(value = "count", required = true, defaultValue = "5")int count,
                          @RequestParam(value = "fullname", required = false) String fullname,
                          @RequestParam(value = "company", required = false)String company,
                          @RequestParam(value = "phone", required = false)String phone,
                          @RequestParam(value = "source", required = false)String source,
                          @RequestParam(value = "owner", required = false)String owner,
                          @RequestParam(value = "mphone", required = false)String mphone,
                          @RequestParam(value = "state", required = false)String state){
        try{
            PageResult result = clueService.list(start, count, fullname, company, phone, source, owner, mphone, state);
            return Result.success(result);
        }catch (ResultException e){
            return Result.fail(e);
        }
    }
    @RequestMapping(value = "delete",method = RequestMethod.POST)
    public Result delete(@RequestBody List<String> ids){
        try{
            clueService.delete(ids);
            return Result.success();
        }catch (ResultException e){
            return Result.fail(e);
        }
    }
    @RequestMapping("edit")
    public Result getById(String id){
        try{
            TblClue clue = clueService.getById(id);
            return Result.success(clue);
        }catch (ResultException e){
            return Result.fail(e);
        }
    }
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Result update(@RequestBody TblClue editClue, HttpServletRequest request){
        //获取修改者
        TblUser user = (TblUser) request.getSession().getAttribute(USER);
        editClue.setEditby(user.getId());
        //修改
        try{
            clueService.update(editClue);
            return Result.success();
        }catch (ResultException e){
            return Result.fail(e);
        }
    }
}
