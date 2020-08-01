package com.example.controller;

import com.example.pojo.TblClue;
import com.example.pojo.TblDicValue;
import com.example.pojo.TblUser;
import com.example.service.TblClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import com.example.utils.*;
import com.example.exception.ResultException;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

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
                          @RequestParam(value = "fullname", required = false)String fullname,
                          @RequestParam(value = "company", required = false)String company,
                          @RequestParam(value = "phone", required = false)String phone,
                          @RequestParam(value = "source", required = false)String source,
                          @RequestParam(value = "owner", required = false)String owner,
                          @RequestParam(value = "mphone", required = false)String mphone,
                          @RequestParam(value = "state", required = false)String state,
                          HttpServletRequest request){
        PageResult result = clueService.list(start, count, fullname, company, phone, source, owner, mphone, state);
        //添加信息
        HashMap<String, Set<TblDicValue>> dicMap =  (HashMap<String, Set<TblDicValue>>)request.getSession().getServletContext().getAttribute("dic");
        List<TblClue> clues = (List<TblClue>)result.getRows();
        for(TblClue clue : clues){
            setClueMsg(dicMap, clue);
        }
        return Result.success(result);
    }

    private void setClueMsg( HashMap<String, Set<TblDicValue>> dicMap, TblClue clue) {
        Set<TblDicValue> appels = dicMap.get("appellation");
        Set<TblDicValue> states = dicMap.get("clueState");
        Set<TblDicValue> sources = dicMap.get("source");
        if(clue.getAppellation() != null && !"".equals(clue.getAppellation())){
            for(TblDicValue value : appels){
                if(clue.getAppellation().equals(value.getId())){
                    clue.setAppelname(value.getValue());
                    break;
                }
            }
        }
        if(clue.getState() != null && !"".equals(clue.getState())){
            for(TblDicValue value : states){
                if(clue.getState().equals(value.getId())){
                    clue.setStatename(value.getValue());
                    break;
                }
            }
        }
        if(clue.getSource() != null && !"".equals(clue.getSource())){
            for(TblDicValue value : sources){
                if(clue.getSource().equals(value.getId())){
                    clue.setSourcename(value.getValue());
                    break;
                }
            }
        }
    }

    @RequestMapping(value = "delete",method = RequestMethod.POST)
    public Result delete(@RequestBody List<String> ids){
        clueService.delete(ids);
        return Result.success();
    }
    @RequestMapping("edit")
    public Result getById(String id, HttpServletRequest request){
        HashMap<String, Set<TblDicValue>> dicMap =  (HashMap<String, Set<TblDicValue>>)request.getSession().getServletContext().getAttribute("dic");
        TblClue clue = clueService.getById(id);
        setClueMsg(dicMap, clue);
        return Result.success(clue);
    }
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Result update(@RequestBody TblClue editClue, HttpServletRequest request){
        //获取修改者
        TblUser user = (TblUser) request.getSession().getAttribute(USER);
        editClue.setEditby(user.getId());
        //修改
        clueService.update(editClue);
        return Result.success();
    }
}
