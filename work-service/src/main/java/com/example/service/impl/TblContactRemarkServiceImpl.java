package com.example.service.impl;

import com.example.enums.ResultEnum;
import com.example.mapper.TblActivityMapper;
import com.example.mapper.TblContactsRemarkMapper;
import com.example.mapper.TblUserMapper;
import com.example.mapper.TblContactsActivityRelationMapper;
import com.example.pojo.*;
import com.example.service.TblContactRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.utils.*;
import com.example.exception.ResultException;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TblContactRemarkServiceImpl implements TblContactRemarkService {

    @Autowired
    private TblContactsRemarkMapper remarkMapper;
    @Autowired
    private TblUserMapper userMapper;
    @Autowired
    private TblContactsActivityRelationMapper relationMapper;
    @Autowired
    private TblActivityMapper activityMapper;

    @Override
    public void add(TblContactsRemark remark) {
        //设置id
        remark.setId(UUIDUtil.getUUID());
        //设置创建时间
        remark.setCreatetime(DateTimeUtil.getSysTime());
        //设置修改标志 0：未修改 1：修改
        remark.setEditflag("0");
        //添加
        try{
            int res = remarkMapper.insert(remark);
        }catch (Exception e){
            throw new ResultException(ResultEnum.FAIL);
        }
    }

    @Override
    public List<TblContactsRemark> list(String id) {
        TblContactsRemarkExample remarkExample = new TblContactsRemarkExample();
        remarkExample.createCriteria().andContactsidEqualTo(id);
        List<TblContactsRemark> remarks = remarkMapper.selectByExample(remarkExample);
        //添加数据
        if(remarks == null && remarks.size() == 0){
            throw new ResultException(ResultEnum.SUCCESS_NODATA);
        }
        for(TblContactsRemark remark : remarks){
            remark.setCreateby(userMapper.selectByPrimaryKey(remark.getCreateby()).getName());
            if(remark.getEditflag().equals("1")){
                remark.setEditby(userMapper.selectByPrimaryKey(remark.getEditby()).getName());
            }
        }
        return remarks;
    }

    @Override
    public void update(TblContactsRemark remark) {
        //创建者置空
        remark.setCreateby(null);
        //添加修改时间
        remark.setEdittime(DateTimeUtil.getSysTime());
        //修改
        try{
            remarkMapper.updateByPrimaryKeySelective(remark);
        }catch (Exception e){
            throw new ResultException(ResultEnum.FAIL);
        }
    }

    @Override
    public void delete(String id) {
        try{
            remarkMapper.deleteByPrimaryKey(id);
        }catch (Exception e){
            throw new ResultException(ResultEnum.FAIL);
        }
    }

    @Override
    public void addRelation(List<String> ids, String id) {
        try{
            for(String rkId : ids){
                TblContactsActivityRelation relation = new TblContactsActivityRelation();
                relation.setId(UUIDUtil.getUUID());
                relation.setActivityid(rkId);
                relation.setContactsid(id);
                relationMapper.insert(relation);
            }
        }catch (Exception e){
            throw new ResultException(ResultEnum.FAIL);
        }
    }

    @Override
    public List<TblActivity> listRelations(String id) {
        List<TblActivity> activities = new ArrayList<>();
        TblContactsActivityRelationExample relationExample = new TblContactsActivityRelationExample();
        relationExample.createCriteria().andContactsidEqualTo(id);
        //根据联系人id查询关联表
        List<TblContactsActivityRelation> relations = relationMapper.selectByExample(relationExample);
        if(relations == null || relations.size() == 0){
            throw new ResultException(ResultEnum.SUCCESS_NODATA);
        }
        //根据关联表集合查询市场活动
        try{
            for(TblContactsActivityRelation relation : relations){
                TblActivity activity = activityMapper.selectByPrimaryKey(relation.getActivityid());
                activity.setOwner(userMapper.selectByPrimaryKey(activity.getOwner()).getName());
                activities.add(activity);
            }
        }catch (Exception e){
            throw new ResultException(ResultEnum.FAIL);
        }
        return activities;
    }

    @Override
    public void deleteRelation(String actId, String ctId) {
        TblContactsActivityRelationExample relationExample = new TblContactsActivityRelationExample();
        relationExample.createCriteria().andActivityidEqualTo(actId).andContactsidEqualTo(ctId);
        try{
            relationMapper.deleteByExample(relationExample);
        }catch (Exception e){
            throw new ResultException(ResultEnum.FAIL);
        }
    }
}
