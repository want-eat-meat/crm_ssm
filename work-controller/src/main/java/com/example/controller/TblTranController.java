package com.example.controller;

import com.example.pojo.TblDicValue;
import com.example.pojo.TblTran;
import com.example.pojo.TblUser;
import com.example.service.TblTranService;
import com.example.service.TblUserService;
import com.example.utils.*;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.sound.midi.Soundbank;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("tran")
public class TblTranController {

    @Autowired
    private TblTranService tranService;
    @Autowired
    private TblUserService userService;

    @Value("${session.user}")
    private String USER;

    @RequestMapping("listMessage")
    public Result listMessage(HttpServletRequest request){
        ServletContext servletContext = request.getSession().getServletContext();
        HashMap<String, Set<TblDicValue>> dic = (HashMap<String, Set<TblDicValue>>) servletContext.getAttribute("dic");

        HashMap<String, Object> result = new HashMap<String, Object>();
        //用户集合
        List<TblUser> users = userService.listAll();
        result.put("users", users);
        //阶段集合
        result.put("stages", dic.get("stage"));
        //类型集合
        result.put("transactionTypes", dic.get("transactionType"));
        //来源集合
       result.put("sources", dic.get("source"));
        return Result.success(result);
    }

    @RequestMapping("list")
    public Result listAll(int start,
                          int count,
                          @RequestParam(required = false) String owner,
                          @RequestParam(required = false) String name,
                          @RequestParam(required = false) String ctmname,
                          @RequestParam(required = false) String stage,
                          @RequestParam(required = false) String type,
                          @RequestParam(required = false) String source,
                          @RequestParam(required = false) String ctname,
                          HttpServletRequest request){
        //查询
        PageInfo pageInfo = tranService.list(start, count, owner, name, ctmname, stage, type, source, ctname);
        ServletContext servletContext = request.getSession().getServletContext();
        //替换数据
        HashMap<String, Set<TblDicValue>> dic = (HashMap<String, Set<TblDicValue>>) servletContext.getAttribute("dic");
        Set<TblDicValue> stages = dic.get("stage");
        Set<TblDicValue> types = dic.get("transactionType");
        Set<TblDicValue> sources = dic.get("source");
        List<TblTran> trans = pageInfo.getList();
        for(TblTran tran : trans){
            for(TblDicValue value : stages){
                if(value.getId().equals(tran.getStage())){
                    tran.setStage(value.getValue());
                    break;
                }
            }
            for(TblDicValue value : types){
                if(value.getId().equals(tran.getType())){
                    tran.setType(value.getValue());
                    break;
                }
            }
            for(TblDicValue value : sources){
                if(value.getId().equals(tran.getSource())){
                    tran.setSource(value.getValue());
                    break;
                }
            }
        }
        PageResult pageResult = new PageResult(pageInfo.getTotal(), pageInfo.getList());
        return Result.success(pageResult);
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Result delete(@RequestBody List<String> ids){
        tranService.delete(ids);
        return Result.success();
    }
}
