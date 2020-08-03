package com.example.service.impl;

import com.example.enums.ResultEnum;
import com.example.exception.ResultException;
import com.example.mapper.*;
import com.example.pojo.*;
import com.example.service.TblTranService;
import com.example.utils.PageResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TblTranServiceImpl implements TblTranService {

    @Autowired
    private TblTranMapper tranMapper;
    @Autowired
    private TblUserMapper userMapper;
    @Autowired
    private TblCustomerMapper customerMapper;
    @Autowired
    private TblContactsMapper contactsMapper;
    @Autowired
    private TblTranRemarkMapper tranRemarkMapper;
    @Autowired
    private TblTranHistoryMapper tranHistoryMapper;

    @Override
    public PageInfo list(int start, int count, String owner, String name, String ctmname, String stage, String type, String source, String ctname) {
        TblTranExample tranExample = new TblTranExample();
        TblTranExample.Criteria criteria = tranExample.createCriteria();
        //添加条件
        if(owner != null && !"".equals(owner)){
            TblUserExample userExample = new TblUserExample();
            userExample.createCriteria().andNameLike("%"+owner+"%");
            List<TblUser> users = userMapper.selectByExample(userExample);
            if(users != null && users.size() != 0){
                List<String> ids = new ArrayList<>();
                for(TblUser user : users){
                    ids.add(user.getId());
                }
                criteria.andOwnerIn(ids);
            }
        }
        if(name != null && !"".equals(name)){
            criteria.andNameLike("%"+name+"%");
        }
        if(ctmname != null && !"".equals(ctmname)){
            TblCustomerExample customerExample = new TblCustomerExample();
            customerExample.createCriteria().andNameLike("%" + ctmname + "%");
            List<TblCustomer> customers = customerMapper.selectByExample(customerExample);
            if(customers !=null && customers.size() != 0){
                List<String> ids = new ArrayList<>();
                for(TblCustomer customer : customers){
                    ids.add(customer.getId());
                }
                criteria.andCustomeridIn(ids);
            }
        }
        if(stage != null && !"".equals(stage)){
            criteria.andStageEqualTo(stage);
        }
        if(type != null && !"".equals(type)){
            criteria.andTypeEqualTo(type);
        }
        if(source != null && !"".equals(source)){
            criteria.andSourceEqualTo(source);
        }
        if(ctname != null && !"".equals(ctname)){
            TblContactsExample contactsExample = new TblContactsExample();
            contactsExample.createCriteria().andFullnameLike("%"+ctname+"%");
            List<TblContacts> contacts = contactsMapper.selectByExample(contactsExample);
            if(contacts != null && contacts.size() != 0){
                List<String> ids = new ArrayList<>();
                for(TblContacts contact : contacts){
                    ids.add(contact.getId());
                }
                criteria.andContactsidIn(ids);
            }
        }
        //分页拦截
        PageHelper.startPage(start, count);
        //查询
        List<TblTran> tblTrans = tranMapper.selectByExample(tranExample);
        //分页信息
        PageInfo pageInfo = new PageInfo(tblTrans);
        List<TblTran> trans= pageInfo.getList();
        //替换数据
        for(TblTran tran : trans){
            setTranData(tran);
        }
        return pageInfo;
    }

    private void setTranData(TblTran tran) {
        TblUser user = userMapper.selectByPrimaryKey(tran.getOwner());
        if(user != null) {
            tran.setOwner(user.getName());
        }
        TblCustomer customer = customerMapper.selectByPrimaryKey(tran.getCustomerid());
        if(customer != null) {
            tran.setCustomerid(customer.getName());
        }
        TblContacts contacts = contactsMapper.selectByPrimaryKey(tran.getContactsid());
        if(contacts != null) {
            tran.setContactsid(contacts.getFullname());
        }
    }

    @Override
    public void delete(List<String> ids) {
        try {
            //删除交易备注
            TblTranRemarkExample tblTranRemarkExample = new TblTranRemarkExample();
            tblTranRemarkExample.createCriteria().andTranidIn(ids);
            tranRemarkMapper.deleteByExample(tblTranRemarkExample);
            //删除交易历史
            TblTranHistoryExample tranHistoryExample = new TblTranHistoryExample();
            tranHistoryExample.createCriteria().andTranidIn(ids);
            tranHistoryMapper.deleteByExample(tranHistoryExample);
            //删除交易
            TblTranExample tranExample = new TblTranExample();
            tranExample.createCriteria().andIdIn(ids);
            tranMapper.deleteByExample(tranExample);
        }catch (Exception e){
            throw new ResultException(ResultEnum.FAIL);
        }
    }
}
