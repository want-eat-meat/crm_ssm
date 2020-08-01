package com.example.service.impl;

import com.example.enums.ResultEnum;
import com.example.exception.ResultException;
import com.example.mapper.TblCustomerMapper;
import com.example.mapper.TblCustomerRemarkMapper;
import com.example.mapper.TblUserMapper;
import com.example.pojo.*;
import com.example.service.TblCustomerService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.utils.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TblCustomerServiceImpl implements TblCustomerService {
    @Autowired
    private TblCustomerMapper customerMapper;
    @Autowired
    private TblUserMapper userMapper;
    @Autowired
    private TblCustomerRemarkMapper remarkMapper;

    @Override
    public void add(TblCustomer customer) {
        //添加id主键
        customer.setId(UUIDUtil.getUUID());
        //添加创建时间
        customer.setCreatetime(DateTimeUtil.getSysTime());
        //判断是否存在
        List<TblCustomer> customers = customerMapper.selectByExample(null);
        for(TblCustomer obj : customers){
            if(obj.getName().equals(customer.getName())){
                throw new ResultException("客户已存在");
            }
        }
        //添加
        try{
            customerMapper.insertSelective(customer);
        }catch (Exception e){
            throw new ResultException(ResultEnum.FAIL);
        }
    }

    @Override
    public PageResult list(int start, int count, String name, String owner, String phone, String website) {
        TblCustomerExample customerExample = new TblCustomerExample();
        TblCustomerExample.Criteria criteria = customerExample.createCriteria();
        //添加查询条件
        if(name != null && !"".equals(name)){
            criteria.andNameLike("%" + name + "%");
        }
        if(owner != null && !"".equals(owner)){
            TblUserExample userExample = new TblUserExample();
            userExample.createCriteria().andNameLike("%" + owner + "%");
            List<TblUser> users = userMapper.selectByExample(userExample);
            if(users.size() != 0){
                List<String> ids = new ArrayList<>();
                for(TblUser user : users){
                    ids.add(user.getId());
                }
                criteria.andOwnerIn(ids);
            }
        }
        if(phone != null && !"".equals(phone)){
            criteria.andPhoneLike("%"+phone+"%");
        }
        if(website != null && !"".equals(website)){
            criteria.andPhoneLike("%"+phone+"%");
        }
        try{
            PageHelper.startPage(start, count);
            List<TblCustomer> customers = customerMapper.selectByExample(customerExample);
            for(TblCustomer customer : customers){
                customer.setOwnername(userMapper.selectByPrimaryKey(customer.getOwner()).getName());
            }
            PageInfo pageInfo = new PageInfo(customers);
            PageResult pageResult = new PageResult(pageInfo.getTotal(), pageInfo.getList());
            return pageResult;
        }catch (Exception e){
            throw new ResultException(ResultEnum.FAIL);
        }
    }

    @Override
    public void delete(List<String> ids) {
        TblCustomerExample customerExample = new TblCustomerExample();
        customerExample.createCriteria().andIdIn(ids);
        TblCustomerRemarkExample remarkExample = new TblCustomerRemarkExample();
        remarkExample.createCriteria().andCustomeridIn(ids);
        try{
            //删除备注
            remarkMapper.deleteByExample(remarkExample);
            //删除客户
            customerMapper.deleteByExample(customerExample);
        }catch (Exception e){
            throw new ResultException(ResultEnum.FAIL);
        }
    }

    @Override
    public TblCustomer getById(String id) {
        try {
            TblCustomer customer = customerMapper.selectByPrimaryKey(id);
            //添加信息
            customer.setOwnername(userMapper.selectByPrimaryKey(customer.getOwner()).getName());
            customer.setCreatename(userMapper.selectByPrimaryKey(customer.getCreateby()).getName());
            if (customer.getEditby() != null && !"".equals(customer.getEditby())) {
                customer.setEditname(userMapper.selectByPrimaryKey(customer.getEditby()).getName());
            }
            return customer;
        }catch (Exception e){
            throw new ResultException(ResultEnum.FAIL);
        }
    }

    @Override
    public void update(TblCustomer customer) {
        //添加修改时间
        customer.setEdittime(DateTimeUtil.getSysTime());
        //修改
        try{
            customerMapper.updateByPrimaryKeySelective(customer);
        }catch (Exception e){
            throw new ResultException(ResultEnum.FAIL);
        }
    }

    @Override
    public List<TblCustomer> list(String name) {
        TblCustomerExample customerExample = new TblCustomerExample();
        if(name != null && !"".equals(name)) {
            customerExample.createCriteria().andNameLike("%" + name + "%");
        }
        List<TblCustomer> customers = customerMapper.selectByExample(customerExample);
        if(customers.size() == 0){
            throw new ResultException(ResultEnum.SUCCESS_NODATA);
        }
        return customers;
    }
}
