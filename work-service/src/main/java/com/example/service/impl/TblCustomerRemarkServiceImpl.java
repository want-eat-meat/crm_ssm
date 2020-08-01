package com.example.service.impl;

import com.example.enums.ResultEnum;
import com.example.mapper.TblContactsMapper;
import com.example.mapper.TblCustomerRemarkMapper;
import com.example.mapper.TblUserMapper;
import com.example.pojo.TblContacts;
import com.example.pojo.TblContactsExample;
import com.example.pojo.TblCustomerRemark;
import com.example.pojo.TblCustomerRemarkExample;
import com.example.service.TblCustomerRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.utils.*;
import com.example.exception.ResultException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TblCustomerRemarkServiceImpl implements TblCustomerRemarkService {
    @Autowired
    private TblCustomerRemarkMapper remarkMapper;
    @Autowired
    private TblUserMapper userMapper;
    @Autowired
    private TblContactsMapper contactsMapper;

    @Override
    public void add(TblCustomerRemark remark) {
        //添加id
        remark.setId(UUIDUtil.getUUID());
        //添加创建事件
        remark.setCreatetime(DateTimeUtil.getSysTime());
        //设置修改标志 0:未修改 1:修改
        remark.setEditflag("0");
        //添加
        int i = remarkMapper.insertSelective(remark);
        if(i != 1){
            throw new ResultException(ResultEnum.FAIL);
        }
    }

    @Override
    public List<TblCustomerRemark> list(String id) {
        TblCustomerRemarkExample remarkExample = new TblCustomerRemarkExample();
        remarkExample.createCriteria().andCustomeridEqualTo(id);
        List<TblCustomerRemark> remarks = remarkMapper.selectByExample(remarkExample);
        if(remarks.size() == 0){
            throw new ResultException(ResultEnum.SUCCESS_NODATA);
        }
        for(TblCustomerRemark remark : remarks){
            remark.setCreateby(userMapper.selectByPrimaryKey(remark.getCreateby()).getName());
            //有被编辑过
            if(!"0".equals(remark.getEditflag())){
                remark.setEditby(userMapper.selectByPrimaryKey(remark.getEditby()).getName());
            }
        }
        return remarks;
    }

    @Override
    public void update(TblCustomerRemark remark) {
        //添加修改时间
        remark.setEdittime(DateTimeUtil.getSysTime());
        //修改修改标志 0:未修改 1:修改
        remark.setEditflag("1");
        //创建者设为空
        remark.setCreateby(null);
        //修改
        int selective = remarkMapper.updateByPrimaryKeySelective(remark);
        if(selective < 1){
            throw new ResultException(ResultEnum.FAIL);
        }
    }

    @Override
    public void delete(String id) {
        int result = remarkMapper.deleteByPrimaryKey(id);
        if(result < 1){
            throw new ResultException(ResultEnum.FAIL);
        }
    }

    @Override
    public void addContact(TblContacts contact) {
        //添加id
        contact.setId(UUIDUtil.getUUID());
        //添加创建时间
        contact.setCreatetime(DateTimeUtil.getSysTime());
        //添加
        try{
            contactsMapper.insert(contact);
        }catch (Exception e){
            throw new ResultException(ResultEnum.FAIL);
        }
    }

    @Override
    public List<TblContacts> listContact(String id) {
        TblContactsExample contactsExample = new TblContactsExample();
        contactsExample.createCriteria().andCustomeridEqualTo(id);
        List<TblContacts> contacts = contactsMapper.selectByExample(contactsExample);
        return contacts;
    }
}
