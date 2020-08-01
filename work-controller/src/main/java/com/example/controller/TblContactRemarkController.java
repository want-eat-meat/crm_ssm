package com.example.controller;

import com.example.pojo.TblContactsRemark;
import com.example.pojo.TblUser;
import com.example.service.TblContactRemarkService;
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
@RequestMapping("contactRemark")
public class TblContactRemarkController {

    @Autowired
    private TblContactRemarkService remarkService;

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
}
