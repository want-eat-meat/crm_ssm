package com.example.service.impl;

import com.example.enums.ResultEnum;
import com.example.mapper.TblContactsRemarkMapper;
import com.example.mapper.TblCustomerMapper;
import com.example.mapper.TblUserMapper;
import com.example.pojo.*;
import com.example.service.TblContactService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.mapper.TblContactsMapper;
import com.example.utils.*;
import com.example.exception.ResultException;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional
public class TblContactServiceImpl implements TblContactService {

    @Autowired
    private TblContactsMapper contactsMapper;
    @Autowired
    private TblCustomerMapper customerMapper;
    @Autowired
    private TblUserMapper userMapper;
    @Autowired
    private TblContactsRemarkMapper remarkMapper;

    @Override
    public void add(TblContacts contact) {
        //添加id
        contact.setId(UUIDUtil.getUUID());
        //添加创建时间
        contact.setCreatetime(DateTimeUtil.getSysTime());
        //查找客户
        TblCustomerExample customerExample = new TblCustomerExample();
        customerExample.createCriteria().andNameEqualTo(contact.getCustomerid());
        List<TblCustomer> customers = customerMapper.selectByExample(customerExample);
        String customerId;
        //客户不存在
        if(customers.size() == 0){
            //新建客户
            customerId = createCutomer(contact, contact.getCreateby(), contact.getCreatetime());
        }else{
            customerId = customers.get(0).getId();
        }
        contact.setCustomerid(customerId);
        //添加
        int result = contactsMapper.insertSelective(contact);

        if(result < 1){
            throw new ResultException(ResultEnum.FAIL);
        }
    }

    private String createCutomer(TblContacts contact, String createBy, String createTime) {
        TblCustomer customer = new TblCustomer();
        String customerId = UUIDUtil.getUUID();
        customer.setId(customerId);
        customer.setOwner(contact.getOwner());
        customer.setName(contact.getCustomerid());
        customer.setCreateby(createBy);
        customer.setCreatetime(createTime);
        customer.setAddress(contact.getAddress());
        int result = customerMapper.insertSelective(customer);
        if(result < 1){
            throw new ResultException(ResultEnum.FAIL);
        }
        return customerId;
    }

    @Override
    public PageResult listAll(int start, int count, String fullname, String owner, String phone, String ctmname, String source, String birth) {
        TblContactsExample contactsExample = new TblContactsExample();
        TblContactsExample.Criteria criteria = contactsExample.createCriteria();
        //添加搜索条件
        if(fullname != null && !"".equals(fullname)){
            criteria.andFullnameLike("%"+fullname+"%");
        }
        if(owner != null && !"".equals(owner)){
            TblUserExample userExample = new TblUserExample();
            userExample.createCriteria().andNameLike("%"+owner+"%");
            List<TblUser> users = userMapper.selectByExample(userExample);
            List<String> ids = new ArrayList<>();
            if(users != null && users.size() != 0){
                for(TblUser user : users){
                    ids.add(user.getId());
                }
                criteria.andOwnerIn(ids);
            }
        }
        if(phone != null && !"".equals(phone)){
            criteria.andMphoneLike("%"+phone+"%");
        }
        if(ctmname != null && !"".equals(ctmname)){
            TblCustomerExample customerExample = new TblCustomerExample();
            customerExample.createCriteria().andNameLike("%"+ctmname+"%");
            List<TblCustomer> customers = customerMapper.selectByExample(customerExample);
            List<String> ids = new ArrayList<>();
            if(customers != null && customers.size() != 0){
                for(TblCustomer customer : customers){
                    ids.add(customer.getId());
                }
                criteria.andCustomeridIn(ids);
            }
        }
        if(source != null && !"".equals(source)){
            criteria.andSourceEqualTo(source);
        }
        if(birth != null && !"".equals(birth)){
            criteria.andBirthLike("%"+birth+"%");
        }
        //开启分页拦截
        PageHelper.startPage(start, count);
        //查询
        List<TblContacts> tblContacts = contactsMapper.selectByExample(contactsExample);
        PageInfo pageInfo = new PageInfo(tblContacts);
        //分页信息
        List<TblContacts> pageInfoList = pageInfo.getList();
        //结果添加信息
        List<HashMap<String, Object>> result = new ArrayList<>();
        for(TblContacts contacts : pageInfoList){
            HashMap<String, Object> contactMap = new HashMap<>();
            contactMap.put("contact", contacts);
            TblUser user = userMapper.selectByPrimaryKey(contacts.getOwner());
            if(user != null) {
                contactMap.put("ownername", user.getName());
            }
            TblCustomer customer = customerMapper.selectByPrimaryKey(contacts.getCustomerid());
            if(customer != null) {
                contactMap.put("customer", customer.getName());
            }
            result.add(contactMap);
        }
        pageInfo.setList(result);
        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public void delete(List<String> ids) {
        TblContactsExample contactsExample = new TblContactsExample();
        contactsExample.createCriteria().andIdIn(ids);
        TblContactsRemarkExample remarkExample = new TblContactsRemarkExample();
        remarkExample.createCriteria().andContactsidIn(ids);
        try{
            //删除备注
            remarkMapper.deleteByExample(remarkExample);
            //删除联系人
            contactsMapper.deleteByExample(contactsExample);
        } catch (Exception e){
            throw new ResultException(ResultEnum.FAIL);
        }
    }

    @Override
    public HashMap<String, Object> getById(String id) {
        HashMap<String, Object> map= new HashMap<>();

        TblContacts contacts = contactsMapper.selectByPrimaryKey(id);
        if(contacts == null){
            throw new ResultException(ResultEnum.SUCCESS_NODATA);
        }
        map.put("contact", contacts);
        //添加信息
        String createname = userMapper.selectByPrimaryKey(contacts.getCreateby()).getName();
        map.put("createname",createname);
        String ownername = userMapper.selectByPrimaryKey(contacts.getOwner()).getName();
        map.put("ownername", ownername);
        if(contacts.getEditby() != null && !"".equals(contacts.getEditby())){
            String editname = userMapper.selectByPrimaryKey(contacts.getEditby()).getName();
            map.put("editname", editname);
        }
        String customername = customerMapper.selectByPrimaryKey(contacts.getCustomerid()).getName();
        map.put("customername", customername);
        return map;
    }

    @Override
    public void update(TblContacts contacts) {
        //添加修改时间
        contacts.setEdittime(DateTimeUtil.getSysTime());
        //判断客户是否存在
        TblCustomerExample customerExample = new TblCustomerExample();
        customerExample.createCriteria().andNameEqualTo(contacts.getCustomerid());
        List<TblCustomer> customers = customerMapper.selectByExample(customerExample);
        String customerId;
        if(customers == null || customers.size() == 0){
            customerId = createCutomer(contacts, contacts.getEditby(), contacts.getEdittime());
        }else{
            customerId = customers.get(0).getId();
        }
        contacts.setCustomerid(customerId);
        //提交修改
        int res = contactsMapper.updateByPrimaryKeySelective(contacts);
        if(res < 1){
            throw new ResultException(ResultEnum.FAIL);
        }
    }
}
