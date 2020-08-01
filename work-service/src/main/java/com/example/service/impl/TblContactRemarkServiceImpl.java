package com.example.service.impl;

import com.example.enums.ResultEnum;
import com.example.mapper.TblContactsRemarkMapper;
import com.example.mapper.TblUserMapper;
import com.example.pojo.TblContactsExample;
import com.example.pojo.TblContactsRemark;
import com.example.pojo.TblContactsRemarkExample;
import com.example.service.TblContactRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.utils.*;
import com.example.exception.ResultException;

import java.util.List;

@Service
@Transactional
public class TblContactRemarkServiceImpl implements TblContactRemarkService {

    @Autowired
    private TblContactsRemarkMapper remarkMapper;
    @Autowired
    private TblUserMapper userMapper;

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
}
