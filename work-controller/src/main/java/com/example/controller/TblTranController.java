package com.example.controller;

import com.example.pojo.*;
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
import java.util.Map;
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

    @RequestMapping("getPoss")
    public Result getPossible(String id, HttpServletRequest request){
        Map poss = (Map) request.getSession().getServletContext().getAttribute("poss");
        String result = (String) poss.get(id);
        return Result.success(result);
    }

    @RequestMapping("listActivity")
    public Result listActivity(int start,
                               int count,
                               @RequestParam(required = false) String search){
        PageResult pageResult = tranService.listActivity(start, count, search);
        return Result.success(pageResult);
    }

    @RequestMapping("listContact")
    public Result listContact(int start,
                               int count,
                               @RequestParam(required = false) String search){
        PageResult pageResult = tranService.listContact(start, count, search);
        return Result.success(pageResult);
    }

    @RequestMapping("listCustomer")
    public Result listCustomer(String name){
        List<Map<String, String>> result = tranService.listCustomer(name);
        return Result.success(result);
    }

    @RequestMapping(value = "addTran", method = RequestMethod.POST)
    public Result add(@RequestBody TblTran tran, HttpServletRequest request){
        //设置创建者
        TblUser user = (TblUser) request.getSession().getAttribute(USER);
        tran.setCreateby(user.getId());
        //添加
        tranService.add(tran);
        return Result.success();
    }

    @RequestMapping("getById")
    public Result getById(String id, HttpServletRequest request){
        Map<String, Object> tranMap = tranService.getById(id);
        TblTran tran = (TblTran) tranMap.get("tran");
        //添加数据
        ServletContext servletContext = request.getSession().getServletContext();
        Map<String, Set<TblDicValue>> dic = (Map<String, Set<TblDicValue>>) servletContext.getAttribute("dic");
        Set<TblDicValue> stages = dic.get("stage");
        Set<TblDicValue> types = dic.get("transactionType");
        Set<TblDicValue> sources = dic.get("source");
        Map poss = (Map) servletContext.getAttribute("poss");
        String stage = getDicValue(stages, tran.getStage());
        tranMap.put("stage", stage);
        if(tran.getType() != null && !"".equals(tran.getType())){
            String type = getDicValue(types, tran.getType());
            tranMap.put("type", type);
        }
        if(tran.getSource() != null && !"".equals(tran.getType())){
            String source = getDicValue(sources, tran.getSource());
            tranMap.put("source", source);
        }
        String possible =  poss.get(tran.getStage()) + "%";
        tranMap.put("possible", possible);
        return  Result.success(tranMap);
    }

    @RequestMapping("listHistory")
    public Result listHistory(String id, HttpServletRequest request){
        ServletContext servletContext = request.getSession().getServletContext();
        Map map = (Map) servletContext.getAttribute("poss");
        Map<String, Set<TblDicValue>> dic = (Map<String, Set<TblDicValue>>) servletContext.getAttribute("dic");
        Set<TblDicValue> stages = dic.get("stage");
        List<TblTranHistory> histories = tranService.listHistory(id);
        for(TblTranHistory history : histories){
            history.setPossible(map.get(history.getStage()) + "%");
            history.setStage(getDicValue(stages, history.getStage()));
        }
        return Result.success(histories);
    }

    private String getDicValue(Set<TblDicValue> values, String target) {
        for(TblDicValue value : values){
            if(value.getId().equals(target)){
                return value.getValue();
            }
        }
        return "";
    }

    @RequestMapping(value = "addRemark", method = RequestMethod.POST)
    public Result addRemark(@RequestBody TblTranRemark remark, HttpServletRequest request){
        //添加创建者
        TblUser user = (TblUser) request.getSession().getAttribute(USER);
        remark.setCreateby(user.getId());
        //添加
        tranService.addRemark(remark);
        return Result.success();
    }

    @RequestMapping("listRemark")
    public Result listRemark(String id){
        List<TblTranRemark> remarks = tranService.listRemark(id);
        return Result.success(remarks);
    }

    @RequestMapping("updateRemark")
    public Result updateRemark(@RequestBody TblTranRemark remark, HttpServletRequest request){
        //设置编辑者
        TblUser user = (TblUser) request.getSession().getAttribute(USER);
        remark.setEditby(user.getId());
        //修改
        tranService.updateRemark(remark);
        return Result.success();
    }

    @RequestMapping("deleteRemark")
    public Result deleteRemark(String id){
        tranService.deleteRemark(id);
        return Result.success();
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Result update(@RequestBody TblTran tran, HttpServletRequest request){
        //设置修改者
        TblUser user = (TblUser) request.getSession().getAttribute(USER);
        tran.setEditby(user.getId());
        //修改
        tranService.update(tran);
        return Result.success();
    }

    @RequestMapping("listStage")
    public Result listStage(String id, HttpServletRequest request){
        ServletContext servletContext = request.getSession().getServletContext();
        Map<String, Set<TblDicValue>> dic = (Map<String, Set<TblDicValue>>) servletContext.getAttribute("dic");
        Set<TblDicValue> stages = dic.get("stage");
        Map<String, String> poss = (Map<String, String>) servletContext.getAttribute("poss");
        List<Map<String, String>> stageList = tranService.listStage(id, stages, poss);
        return Result.success(stageList);
    }

    @RequestMapping("updateStage")
    public Result updateStage(@RequestBody TblTran tran, HttpServletRequest request){
        //设置修改者
        TblUser user = (TblUser) request.getSession().getAttribute(USER);
        tran.setEditby(user.getId());
        //修改
        tranService.updateStage(tran);
        return Result.success();
    }
}
