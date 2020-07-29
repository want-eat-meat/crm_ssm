package com.example.service.impl;

import com.example.enums.ResultEnum;
import com.example.exception.ResultException;
import com.example.mapper.TblUserMapper;
import com.example.pojo.TblUser;
import com.example.pojo.TblUserExample;
import com.example.service.TblUserService;
import com.example.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TblUserServiceImpl implements TblUserService {
    @Autowired
    TblUserMapper userMapper;

    @Override
    public TblUser login(String loginAct, String loginPwd, String ip) {
        //判断是否为空
        if(loginAct == null || "".equals(loginAct) || loginPwd == null || "".equals(loginPwd)){
            throw new ResultException("账号或密码为空");
        }
        //查询数据
        TblUserExample userExample = new TblUserExample();
        TblUserExample.Criteria criteria = userExample.createCriteria();
        criteria.andLoginactEqualTo(loginAct);
        criteria.andLoginpwdEqualTo(loginPwd);
        List<TblUser> tblUsers = userMapper.selectByExample(userExample);
        //判断是否有结果
        if(tblUsers == null || tblUsers.size() == 0){
            throw new ResultException("账号或密码错误");
        }
        TblUser user= tblUsers.get(0);
        //检查账号时间是否到期
        if(DateTimeUtil.getSysTime().compareTo(user.getExpiretime()) > 0){
            throw new ResultException("账号已过期");
        }
        //检查账号状态 0:允许登录 1:禁止登录
        if("1".equals(user.getLockstate())){
            throw new ResultException("账号被锁定");
        }
        //检查ip是否被允许
        if(!user.getAllowips().contains(ip)){
            throw new ResultException("当前IP不允许访问");
        }
        return user;
    }

    @Override
    public void updatePwd(String id, String oldPwd, String newPwd) {
        //根据id获取用户
        TblUser user = userMapper.selectByPrimaryKey(id);
        //判断旧密码是否正确
        if(!oldPwd.equals(user.getLoginpwd())){
            throw new ResultException("旧密码不正确");
        }
        //存储新密码
        user.setLoginpwd(newPwd);
        userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public TblUser getById(String id) {
        TblUser user = userMapper.selectByPrimaryKey(id);
        if(user == null){
            throw new ResultException(ResultEnum.SUCCESS_NODATA);
        }
        return user;
    }

    @Override
    public List<TblUser> listAll() {
        try{
            List<TblUser> users = userMapper.selectByExample(null);
            if(users.size() == 0){
                throw new ResultException(ResultEnum.SUCCESS_NODATA);
            }
            return users;
        }catch (Exception e){
            throw new ResultException(ResultEnum.FAIL);
        }
    }
}
