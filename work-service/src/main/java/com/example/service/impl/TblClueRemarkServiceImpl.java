package com.example.service.impl;

import com.example.enums.ResultEnum;
import com.example.mapper.TblActivityMapper;
import com.example.mapper.TblClueActivityRelationMapper;
import com.example.mapper.TblClueRemarkMapper;
import com.example.mapper.TblUserMapper;
import com.example.pojo.*;
import com.example.service.TblClueRemarkService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.utils.*;
import com.example.exception.ResultException;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TblClueRemarkServiceImpl implements TblClueRemarkService {
    @Autowired
    private TblClueRemarkMapper remarkMapper;
    @Autowired
    private TblUserMapper userMapper;
    @Autowired
    private TblActivityMapper activityMapper;
    @Autowired
    private TblClueActivityRelationMapper relationMapper;

    @Override
    public void add(TblClueRemark remark) {
        //添加主键
        remark.setId(UUIDUtil.getUUID());
        //添加创建时间
        remark.setCreatetime(DateTimeUtil.getSysTime());
        //添加修改标志 0：未修改 1：已修改
        remark.setEditflag("0");
        //添加
        try{
            remarkMapper.insertSelective(remark);
        }catch (Exception e){
            throw new ResultException(ResultEnum.FAIL);
        }
    }

    @Override
    public List<TblClueRemark> list(String id) {
        try{
            TblClueRemarkExample remarkExample = new TblClueRemarkExample();
            remarkExample.createCriteria().andClueidEqualTo(id);
            List<TblClueRemark> remarks = remarkMapper.selectByExample(remarkExample);
            for(TblClueRemark remark : remarks){
                remark.setCreatename(userMapper.selectByPrimaryKey(remark.getCreateby()).getName());
                //判断是否有修改 0：未修改 1：修改
                if(!"0".equals(remark.getEditflag())){
                    remark.setEditname(userMapper.selectByPrimaryKey(remark.getEditby()).getName());
                }
            }
            return remarks;
        }catch (Exception e){
            throw new ResultException(ResultEnum.FAIL);
        }
    }

    @Override
    public void update(TblClueRemark remark) {
        //添加修改时间
        remark.setEdittime(DateTimeUtil.getSysTime());
        //修改添加标记 0：未修改 1：已修改
        remark.setEditflag("1");
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
    public void addRelation(List<String> acts, String clueId) {
        try {
            for (String act : acts) {
                TblClueActivityRelation relation = new TblClueActivityRelation();
                relation.setActivityid(act);
                relation.setClueid(clueId);
                relation.setId(UUIDUtil.getUUID());
                relationMapper.insert(relation);
            }
        }catch (Exception e){
            throw new ResultException(ResultEnum.FAIL);
        }
    }

    @Override
    public List<TblActivity> listRelations(String id) {
        List<TblActivity> activities = new ArrayList<>();
        //根据clue的id查询关联表
        TblClueActivityRelationExample relationExample = new TblClueActivityRelationExample();
       try {
           relationExample.createCriteria().andClueidEqualTo(id);
           List<TblClueActivityRelation> relations = relationMapper.selectByExample(relationExample);
           //根据关联中的activity的id查询activity
           for (TblClueActivityRelation relation : relations) {
               TblActivity activity = activityMapper.selectByPrimaryKey(relation.getActivityid());
               //添加所有者
               activity.setOwnername(userMapper.selectByPrimaryKey(activity.getOwner()).getName());
               //添加到集合中
               activities.add(activity);
           }
           return activities;
       }catch (Exception e){
           throw new ResultException(ResultEnum.FAIL);
       }
    }

    @Override
    public void deleteRelation(String actId, String clueId) {
        try {
            TblClueActivityRelationExample relationExample = new TblClueActivityRelationExample();
            TblClueActivityRelationExample.Criteria criteria = relationExample.createCriteria();
            criteria.andActivityidEqualTo(actId);
            criteria.andClueidEqualTo(clueId);
            relationMapper.deleteByExample(relationExample);
        }catch (Exception e){
            throw new ResultException(ResultEnum.FAIL);
        }
    }

    @Override
    public PageResult listActsBySearch(int start, int count, String id, String search) {
        //根据clue的id查询关联表
        TblClueActivityRelationExample relationExample = new TblClueActivityRelationExample();

        relationExample.createCriteria().andClueidEqualTo(id);
        List<TblClueActivityRelation> relations = relationMapper.selectByExample(relationExample);
        //根据关联中的activity的id查询activity
        List<String> ids = new ArrayList<>();
        for (TblClueActivityRelation relation : relations) {
            ids.add(relation.getActivityid());
        }
        TblActivityExample activityExample = new TblActivityExample();
        TblActivityExample.Criteria activityCriteria = activityExample.createCriteria();
        activityCriteria.andIdIn(ids);
        if(search != null && !"".equals(search)){
            activityCriteria.andNameLike("%"+search+"%");
        }
        PageHelper.startPage(start, count);
        List<TblActivity> activities = activityMapper.selectByExample(activityExample);
        PageInfo pageInfo = new PageInfo(activities);
        List<TblActivity> list = pageInfo.getList();
        for(TblActivity activity: list){
            activity.setOwner(userMapper.selectByPrimaryKey(activity.getOwner()).getName());
        }
        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }
}
