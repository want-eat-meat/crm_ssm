package com.example.service.impl;

import com.example.enums.ResultEnum;
import com.example.exception.ResultException;
import com.example.mapper.*;
import com.example.mapper.TblUserMapper;
import com.example.pojo.*;
import com.example.service.TblActivityService;
import com.example.utils.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
@Transactional
@Service
public class TblActivityServiceImpl implements TblActivityService {

    @Autowired
    TblUserMapper userMapper;
    @Autowired
    TblActivityMapper activityMapper;
    @Autowired
    TblActivityRemarkMapper remarkMapper;
    @Override
    public List<TblUser> getUserList() {
        List<TblUser> users = userMapper.selectByExample(null);
        if(users == null || users.isEmpty()){
            throw new ResultException(ResultEnum.SUCCESS_NODATA);
        }
        return users;
    }

    @Override
    public void add(TblActivity activity) {
        //添加数据
        activity.setId(UUIDUtil.getUUID());
        activity.setCreatetime(DateTimeUtil.getSysTime());
        //添加到数据库
        try{
            activityMapper.insert(activity);
        }catch (Exception e){
            throw new ResultException(ResultEnum.FAIL);
        }
    }

    @Override
    public PageResult list(String name, String owner, String startDate, String endDate, int start, int count) {
        //创建activity example
        TblActivityExample activityExample = new TblActivityExample();
        TblActivityExample.Criteria criteria = activityExample.createCriteria();
        //添加条件
        if(name != null && !"".equals(name)){
            criteria.andNameLike("%" + name + "%");
        }
        if(owner != null && !"".equals(owner)){
            TblUserExample userExample = new TblUserExample();
            userExample.createCriteria().andNameLike("%" + owner + "%");
            List<TblUser> tblUsers = userMapper.selectByExample(userExample);
            List<String> userIds = new ArrayList<>();
            for(TblUser user : tblUsers) {
                userIds.add(user.getId());
            }
            if(userIds.size() != 0) {
                criteria.andOwnerIn(userIds);
            }
        }
        if(startDate != null && !"".equals(startDate)){
            criteria.andStartdateGreaterThanOrEqualTo(startDate);
        }
        if(endDate != null && !"".equals(endDate)){
            criteria.andEnddateLessThanOrEqualTo(endDate);
        }
        //分页拦截
        PageHelper.startPage(start, count);
        //查询结果
        List<TblActivity> tblActivities = activityMapper.selectByExample(activityExample);
        //给所有者赋值
        for(TblActivity activity : tblActivities){
            setUserName(activity);
        }
        //分页信息
        PageInfo pageInfo = new PageInfo(tblActivities);
        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public void delete(List<String> data) {
        //没有数据
        if(data.size() == 0){
            throw new ResultException("未选择数据");
        }
        TblActivityExample activityExample = new TblActivityExample();
        activityExample.createCriteria().andIdIn(data);
        TblActivityRemarkExample remarkExample = new TblActivityRemarkExample();
        remarkExample.createCriteria().andActivityidIn(data);
        try{
            //删除备注
            remarkMapper.deleteByExample(remarkExample);
            //删除活动
            activityMapper.deleteByExample(activityExample);
        }catch (Exception e){
            throw new ResultException(ResultEnum.FAIL);
        }
    }

    @Override
    public TblActivity getById(String id) {
        TblActivity activity;
        try{
            activity = activityMapper.selectByPrimaryKey(id);
            setUserName(activity);
            return activity;
        }catch (Exception e){
            throw new ResultException(ResultEnum.FAIL);
        }
    }

    @Override
    public void update(TblActivity activity) {
        activity.setEdittime(DateTimeUtil.getSysTime());
        try{
            activityMapper.updateByPrimaryKeySelective(activity);
        }catch (Exception e) {
            throw new ResultException(ResultEnum.FAIL);
        }
    }

    /**
     * 给activity的ownerr、create和editer赋值
     * @param activity
     */
    private void setUserName(TblActivity activity) {
        if (activity.getOwner() != null && !"".equals(activity.getOwner())) {
            activity.setOwnername(userMapper.selectByPrimaryKey(activity.getOwner()).getName());
        }
        if (activity.getCreateby() != null && !"".equals(activity.getCreateby())) {
            activity.setCreatename(userMapper.selectByPrimaryKey(activity.getCreateby()).getName());
        }
        if (activity.getEditby() != null && !"".equals(activity.getEditby())) {
            activity.setEditname(userMapper.selectByPrimaryKey(activity.getEditby()).getName());
        }
    }

    @Override
    public List<TblActivity> listAct(String data, List<String> ids) {
        TblActivityExample activityExample = new TblActivityExample();
        TblActivityExample.Criteria criteria = activityExample.createCriteria();
        if(data != null && !"".equals(data)){
            criteria.andNameLike("%"+data+"%");
        }
        if(ids.size() != 0){
            criteria.andIdNotIn(ids);
        }
        try{
            List<TblActivity> activities = activityMapper.selectByExample(activityExample);
            for(TblActivity activity : activities){
                activity.setOwnername(userMapper.selectByPrimaryKey(activity.getOwner()).getName());
            }
            return activities;
        }catch (Exception e){
            throw new ResultException(ResultEnum.FAIL);
        }
    }
}
