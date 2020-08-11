package com.example.controller;

import com.example.exception.ResultException;
import com.example.pojo.TblUser;
import com.example.service.TblUserService;
import com.example.utils.Result;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import com.example.utils.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("user")
public class TblUserController {

    @Autowired
    TblUserService userService;

    @Value("${session.user}")
    private String USER;

    @RequestMapping("login")
    public Result login(String loginAct, String loginPwd, String code, HttpServletRequest request){
        String targetCode = (String) request.getSession().getAttribute("code");
        TblUser user = userService.login(loginAct, MD5Util.getMD5(loginPwd), request.getRemoteAddr(), code, targetCode);
        //存放到Session中
        request.getSession().setAttribute(USER, user);
        return Result.success();
    }

    @RequestMapping("getBySession")
    public Result getUserBySession(HttpServletRequest request){
        TblUser user = (TblUser) request.getSession().getAttribute(USER);
        return Result.success(user);
    }

    @RequestMapping("unLogin")
    public Result unLogin(HttpServletRequest request){
        request.getSession().removeAttribute(USER);
        return Result.success();
    }

    @RequestMapping("changePwd")
    public Result changePwd( String id, String oldPwd, String newPwd, HttpServletRequest request){
        userService.updatePwd(id, MD5Util.getMD5(oldPwd), MD5Util.getMD5(newPwd));
        //关系Session中的值
        TblUser user = (TblUser) request.getSession().getAttribute(USER);
        user.setLoginpwd(MD5Util.getMD5(newPwd));
        request.getSession().setAttribute(USER, user);
        return Result.success(user);
    }

    @RequestMapping("phoneCheck")
    public Result checkPhone(@RequestParam("phone")String phone, HttpServletRequest request){
        String code = userService.checkPhone(phone);
        request.getSession().setAttribute("code", code);
        return Result.success();
    }
}
