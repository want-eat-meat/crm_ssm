package com.example.controller;

import com.example.mapper.TblTranMapper;
import com.example.pojo.*;
import com.example.service.TblActivityService;
import com.example.service.TblContactRemarkService;
import com.example.service.TblTranService;
import com.example.utils.PageResult;
import com.example.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("contactRemark")
public class TblContactRemarkController {

    @Autowired
    private TblContactRemarkService remarkService;
    @Autowired
    private TblActivityService activityService;
    @Autowired
    private TblTranService tranService;

    @Value("${session.user}")
    private String USER;

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Result add(@RequestBody TblContactsRemark remark, HttpServletRequest request){
        //设置创建者
        TblUser user = (TblUser)request.getSession().getAttribute(USER);
        remark.setCreateby(user.getId());
        //添加
        remarkService.add(remark);
        return Result.success();
    }

    @RequestMapping("list")
    public Result list(String id){
        List<TblContactsRemark> remarks = remarkService.list(id);
        return Result.success(remarks);
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Result update(@RequestBody TblContactsRemark remark, HttpServletRequest request){
        //添加修改者
        TblUser user = (TblUser) request.getSession().getAttribute(USER);
        remark.setEditby(user.getId());
        //修改
        remarkService.update(remark);
        return Result.success();
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Result delete(String id){
        remarkService.delete(id);
        return Result.success();
    }

    @RequestMapping(value = "listAct", method = RequestMethod.POST)
    public Result listAct(@RequestParam("data") String data,
                          @RequestParam("ids") List<String> ids,
                          @RequestParam("start") int start,
                          @RequestParam("count") int count){
        PageResult pageResult = activityService.listAct(data, ids, start, count);
        return Result.success(pageResult);
    }

    @RequestMapping("addRelation")
    public Result addRelation(
            @RequestParam("ids") List<String> ids,
            @RequestParam("id") String id){
        remarkService.addRelation(ids, id);
        return Result.success();
    }

    @RequestMapping("listRelations")
    public Result listRelations(String id){
        List<TblActivity> activities = remarkService.listRelations(id);
        return Result.success(activities);
    }

    @RequestMapping("deleteRelation")
    public Result deleteRelation(String ActId, String CtId){
        remarkService.deleteRelation(ActId, CtId);
        return Result.success();
    }

    @RequestMapping("listTran")
    public Result listTrans(String id, HttpServletRequest request){
        List<TblTran> trans = remarkService.listTrans(id);
        //添加数据
        ServletContext servletContext = request.getSession().getServletContext();
        Map<String, String> poss = (Map<String, String>) servletContext.getAttribute("poss");
        HashMap<String, Set<TblDicValue>> dic = (HashMap<String, Set<TblDicValue>>) servletContext.getAttribute("dic");
        Set<TblDicValue> stages = dic.get("stage");
        Set<TblDicValue> types = dic.get("transactionType");
        for(TblTran tran : trans){
            if(tran.getStage() != null && !"".equals(tran.getStage())){
                tran.setPossible(poss.get(tran.getStage()));
                for(TblDicValue value : stages){
                    if(value.getId().equals(tran.getStage())){
                        tran.setStage(value.getValue());
                        break;
                    }
                }
            }
            if(tran.getType() != null && !"".equals(tran.getType())){
                for(TblDicValue value : types){
                    if(value.getId().equals(tran.getType())){
                        tran.setType(value.getValue());
                        break;
                    }
                }
            }
        }
        return Result.success(trans);
    }

    @RequestMapping("deleteTran")
    public Result deleteTran(String id){
        List<String> ids = new ArrayList<>();
        ids.add(id);
        tranService.delete(ids);
        return Result.success();
    }
}
