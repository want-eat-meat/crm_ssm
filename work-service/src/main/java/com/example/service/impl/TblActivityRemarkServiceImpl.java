package com.example.service.impl;

import com.example.enums.ResultEnum;
import com.example.exception.ResultException;
import com.example.mapper.TblUserMapper;
import com.example.pojo.TblActivityRemark;
import com.example.pojo.TblActivityRemarkExample;
import com.example.pojo.TblUser;
import com.example.service.TblActivityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.mapper.TblActivityRemarkMapper;
import com.example.utils.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TblActivityRemarkServiceImpl implements TblActivityRemarkService {
    @Autowired
    TblActivityRemarkMapper activityRemarkMapper;
    @Autowired
    TblUserMapper userMapper;
    @Override
    public void add(TblActivityRemark remark) {
        //添加创建时间
        remark.setCreatetime(DateTimeUtil.getSysTime());
        //添加ID
        remark.setId(UUIDUtil.getUUID());
        //设置修改标志 0:未修改 1：已修改
        remark.setEditflag("0");
        //插入
        try{
            activityRemarkMapper.insertSelective(remark);
        }catch (Exception e){
            throw new ResultException(ResultEnum.FAIL);
        }
    }

    @Override
    public List<TblActivityRemark> list(String id) {
        TblActivityRemarkExample example = new TblActivityRemarkExample();
        example.createCriteria().andActivityidEqualTo(id);
        List<TblActivityRemark> tblActivityRemarks = activityRemarkMapper.selectByExample(example);
        if(tblActivityRemarks.size() == 0){
            throw new ResultException(ResultEnum.SUCCESS_NODATA);
        }
        //添加创建者和编辑者数据
        for(TblActivityRemark remark : tblActivityRemarks){
            TblUser user = userMapper.selectByPrimaryKey(remark.getCreateby());
            remark.setCreatename(user.getName());
            if(remark.getEditby() != null && !"".equals(remark.getEditby())) {
                TblUser user1 = userMapper.selectByPrimaryKey(remark.getEditby());
                remark.setEditname(user1.getName());
            }
        }
        return tblActivityRemarks;
    }

    @Override
    public void update(TblActivityRemark remark) {
        //添加修改日期
        remark.setEdittime(DateTimeUtil.getSysTime());
        //改变修改标志 0:未修改 1:已修改
        remark.setEditflag("1");
        //更改
        try{
            activityRemarkMapper.updateByPrimaryKeySelective(remark);
        }catch (Exception e){
            throw new ResultException(ResultEnum.FAIL);
        }
    }

    @Override
    public void delete(String id) {
        try{
            activityRemarkMapper.deleteByPrimaryKey(id);
        }catch (Exception e){
            throw new ResultException(ResultEnum.FAIL);
        }
    }
}
