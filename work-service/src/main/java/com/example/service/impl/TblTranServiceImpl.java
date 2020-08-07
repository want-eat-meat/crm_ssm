package com.example.service.impl;

import com.example.enums.ResultEnum;
import com.example.exception.ResultException;
import com.example.mapper.*;
import com.example.pojo.*;
import com.example.service.TblTranService;
import com.example.utils.DateTimeUtil;
import com.example.utils.PageResult;
import com.example.utils.UUIDUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    @Autowired
    private TblActivityMapper activityMapper;
    @Autowired
    private TblTranRemarkMapper remarkMapper;

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

    @Override
    public PageResult listActivity(int start, int count, String search) {
        TblActivityExample activityExample = new TblActivityExample();
        if(search != null && !"".equals(search)){
            activityExample.createCriteria().andNameLike("%"+search+"%");
        }
        //分页拦截
        PageHelper.startPage(start, count);
        List<TblActivity> activities = activityMapper.selectByExample(activityExample);
        PageInfo pageInfo = new PageInfo(activities);
        List<TblActivity> datas = pageInfo.getList();
        for(TblActivity activity : datas){
            activity.setOwner(userMapper.selectByPrimaryKey(activity.getOwner()).getName());
        }
        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public PageResult listContact(int start, int count, String search) {
        TblContactsExample contactsExample = new TblContactsExample();
        if(search != null && !"".equals(search)){
            contactsExample.createCriteria().andFullnameLike("%" + search + "%");
        }
        PageHelper.startPage(start, count);
        List<TblContacts> tblContacts = contactsMapper.selectByExample(contactsExample);
        PageInfo pageInfo = new PageInfo(tblContacts);
        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public List<Map<String, String>> listCustomer(String name) {
        TblCustomerExample customerExample = new TblCustomerExample();
        if(name != null && !"".equals(name)){
            customerExample.createCriteria().andNameLike("%"+name+"%");
        }
        List<TblCustomer> tblCustomers = customerMapper.selectByExample(customerExample);
        if(tblCustomers == null || tblCustomers.size() == 0){
            throw new ResultException(ResultEnum.SUCCESS_NODATA);
        }
        List<Map<String, String>> result = new ArrayList<>();
        for(TblCustomer customer : tblCustomers){
            Map<String, String> map = new HashMap<>();
            map.put("name", customer.getName());
            result.add(map);
        }
        return result;
    }

    @Override
    public void add(TblTran tran) {
        if(tran.getOwner() == null || "".equals(tran.getOwner()) ||
        tran.getName() == null || "".equals(tran.getName()) ||
        tran.getExpecteddate() == null || "".equals(tran.getExpecteddate()) ||
        tran.getCustomerid() == null || "".equals(tran.getCustomerid()) ||
        tran.getStage() == null || "".equals(tran.getStage())){
            throw new ResultException("请完善必要信息");
        }
        //设置id
        tran.setId(UUIDUtil.getUUID());
        //添加创建时间
        tran.setCreatetime(DateTimeUtil.getSysTime());
        //判断客户是否存在
        TblCustomerExample customerExample = new TblCustomerExample();
        customerExample.createCriteria().andNameEqualTo(tran.getCustomerid());
        List<TblCustomer> tblCustomers = customerMapper.selectByExample(customerExample);
        if(tblCustomers == null || tblCustomers.size() == 0){
            //客户不存在
            TblCustomer customer = new TblCustomer();
            customer.setId(UUIDUtil.getUUID());
            customer.setName(tran.getCustomerid());
            customer.setCreateby(tran.getCreateby());
            customer.setCreatetime(tran.getCreatetime());
            customer.setOwner(tran.getOwner());
            customerMapper.insertSelective(customer);
            tran.setCustomerid(customer.getId());
        }else{
            //客户存在
            tran.setCustomerid(tblCustomers.get(0).getId());
        }
        try{
            //添加交易历史
            TblTranHistory history = new TblTranHistory();
            history.setId(UUIDUtil.getUUID());
            history.setCreateby(tran.getCreateby());
            history.setCreatetime(tran.getCreatetime());
            history.setExpecteddate(tran.getExpecteddate());
            history.setTranid(tran.getId());
            history.setStage(tran.getStage());
            if(tran.getMoney() != null && !"".equals(tran.getMoney())){
                history.setMoney(tran.getMoney());
            }
            tranHistoryMapper.insertSelective(history);
            //添加交易
            tranMapper.insertSelective(tran);
        }catch (Exception e){
            throw new ResultException(ResultEnum.FAIL);
        }
    }

    @Override
    public Map<String, Object> getById(String id) {
        Map<String, Object> tranMap = new HashMap<>();
        TblTran tran = tranMapper.selectByPrimaryKey(id);
        if(tran == null){
            throw new ResultException(ResultEnum.FAIL);
        }
        tranMap.put("tran", tran);
        //添加数据
        String owner = userMapper.selectByPrimaryKey(tran.getOwner()).getName();
        tranMap.put("owner", owner);
        String customer = customerMapper.selectByPrimaryKey(tran.getCustomerid()).getName();
        tranMap.put("customer", customer);
        if(tran.getActivityid() != null && !"".equals(tran.getActivityid())){
            String activity = activityMapper.selectByPrimaryKey(tran.getActivityid()).getName();
            tranMap.put("activity", activity);
        }
        if(tran.getContactsid() != null && !"".equals(tran.getContactsid())){
            String contact = contactsMapper.selectByPrimaryKey(tran.getContactsid()).getFullname();
            tranMap.put("contact", contact);
        }
        String create = userMapper.selectByPrimaryKey(tran.getCreateby()).getName();
        tranMap.put("create", create);
        if(tran.getEditby() != null && !"".equals(tran.getEditby())){
            String edit = userMapper.selectByPrimaryKey(tran.getEditby()).getName();
            tranMap.put("edit", edit);
        }

        return tranMap;
    }

    @Override
    public List<TblTranHistory> listHistory(String id) {
        TblTranHistoryExample historyExample = new TblTranHistoryExample();
        historyExample.createCriteria().andTranidEqualTo(id);
        historyExample.setOrderByClause("createtime desc");
        List<TblTranHistory> histories = tranHistoryMapper.selectByExample(historyExample);
        if(histories == null || histories.size() == 0){
            throw new ResultException(ResultEnum.SUCCESS_NODATA);
        }
        for(TblTranHistory history : histories){
            history.setCreateby(userMapper.selectByPrimaryKey(history.getCreateby()).getName());
        }
        return histories;
    }

    @Override
    public void addRemark(TblTranRemark remark) {
        //设置id
        remark.setId(UUIDUtil.getUUID());
        //设置创建时间
        remark.setCreatetime(DateTimeUtil.getSysTime());
        //设置修改标志 0：未修改 1：修改
        remark.setEditflag("0");
        //添加
        remarkMapper.insertSelective(remark);
    }

    @Override
    public List<TblTranRemark> listRemark(String id) {
        TblTranRemarkExample remarkExample = new TblTranRemarkExample();
        remarkExample.createCriteria().andTranidEqualTo(id);
        List<TblTranRemark> remarks = remarkMapper.selectByExample(remarkExample);
        if(remarks == null && remarks.size() == 0){
            throw new ResultException(ResultEnum.SUCCESS_NODATA);
        }
        for(TblTranRemark remark : remarks){
            remark.setCreateby(userMapper.selectByPrimaryKey(remark.getCreateby()).getName());
            if("1".equals(remark.getEditflag())){
                remark.setEditby(userMapper.selectByPrimaryKey(remark.getEditby()).getName());
            }
        }
        return remarks;
    }

    @Override
    public void updateRemark(TblTranRemark remark) {
        //设置修改时间
        remark.setEdittime(DateTimeUtil.getSysTime());
        //创建者置空
        remark.setCreateby(null);
        //修改修改标志 0：未修改 1：修改
        remark.setEditflag("1");
        //修改
        try{
            remarkMapper.updateByPrimaryKeySelective(remark);
        }catch (Exception e){
            throw new ResultException(ResultEnum.FAIL);
        }
    }

    @Override
    public void deleteRemark(String id) {
        try{
            remarkMapper.deleteByPrimaryKey(id);
        }catch (Exception e){
            throw new ResultException(ResultEnum.FAIL);
        }
    }

    @Override
    public void update(TblTran tran) {
        //判断市场活动和联系人有无变化
        if("".equals(tran.getActivityid())){
            tran.setActivityid(null);
        }
        if("".equals(tran.getContactsid())){
            tran.setContactsid(null);
        }
        //设置修改时间
        tran.setEdittime(DateTimeUtil.getSysTime());
        //判断客户是否存在
        TblCustomerExample customerExample = new TblCustomerExample();
        customerExample.createCriteria().andNameEqualTo(tran.getCustomerid());
        List<TblCustomer> tblCustomers = customerMapper.selectByExample(customerExample);
        if(tblCustomers == null || tblCustomers.size() == 0){
            //客户不存在
            TblCustomer customer = new TblCustomer();
            customer.setId(UUIDUtil.getUUID());
            customer.setName(tran.getCustomerid());
            customer.setCreateby(tran.getCreateby());
            customer.setCreatetime(tran.getCreatetime());
            customer.setOwner(tran.getOwner());
            customerMapper.insertSelective(customer);
            tran.setCustomerid(customer.getId());
        }else{
            //客户存在
            tran.setCustomerid(tblCustomers.get(0).getId());
        }
        //判断阶段是否变化
        TblTran hisTran = tranMapper.selectByPrimaryKey(tran.getId());
        if(!tran.getStage().equals(hisTran.getStage())){
            //阶段变化，添加历史
            addTranHistory(tran);
        }
        //更新交易
        tranMapper.updateByPrimaryKeySelective(tran);
    }

    private void addTranHistory(TblTran tran) {
        TblTranHistory history= new TblTranHistory();
        history.setId(UUIDUtil.getUUID());
        history.setCreateby(tran.getEditby());
        history.setCreatetime((tran.getEdittime()));
        history.setMoney(tran.getMoney());
        history.setExpecteddate(tran.getExpecteddate());
        history.setTranid(tran.getId());
        history.setStage(tran.getStage());
        tranHistoryMapper.insertSelective(history);
    }

    @Override
    public void updateStage(TblTran tran) {
        //判断阶段是否变化
        TblTran hisTran = tranMapper.selectByPrimaryKey(tran.getId());
        if(tran.getStage().equals(hisTran.getStage())){
            return;
        }
        //添加修改时间
        tran.setEdittime(DateTimeUtil.getSysTime());
        //添加历史
        addTranHistory(tran);
        //修改
        tranMapper.updateByPrimaryKeySelective(tran);
    }

    @Override
    public List<Map<String, String>> listStage(String id, Set<TblDicValue> stages, Map<String, String> poss) {
        TblTran tran = tranMapper.selectByPrimaryKey(id);
        //获取可能性大于0的阶段个数
        int number = 0;
        for(String key : poss.keySet()){
            if(poss.get(key).compareTo("0") > 0){
                number++;
            }
        }

        List<Map<String, String>> stageList = new ArrayList<>();
        //获取当前交易阶段的可能性
        String possible = poss.get(tran.getStage());
        //获取当前交易的排序
        String tranNo = "0";
        for(TblDicValue value : stages){
            if(value.getId().equals(tran.getStage())){
                tranNo = value.getOrderno();
            }
        }
        //给每个阶段赋值
        // 1：绿圈 2：锚点 3：灰圈 4：灰叉 5：红叉
        if(possible.equals("0")){
            //可能性为0
            for(TblDicValue value : stages){
                Map<String, String> stageMap = new HashMap<>();
                stageMap.put("id", value.getId());
                stageMap.put("text", value.getText());
                if(Integer.parseInt(value.getOrderno()) <= number){
                    stageMap.put("type", "3");
                }else{
                    if(value.getId().equals(tran.getStage())){
                        stageMap.put("type", "5");
                    }else{
                        stageMap.put("type", "4");
                    }
                }
                stageList.add(stageMap);
            }
        }else{
            //可能性不为0
            for(TblDicValue value : stages) {
                Map<String, String> stageMap = new HashMap<>();
                stageMap.put("id", value.getId());
                stageMap.put("text", value.getText());
                if(Integer.parseInt(value.getOrderno()) < Integer.parseInt(tranNo)){
                    stageMap.put("type", "1");
                }else if(Integer.parseInt(value.getOrderno()) == Integer.parseInt(tranNo)){
                    stageMap.put("type", "2");
                }else if(Integer.parseInt(value.getOrderno()) <= number){
                    stageMap.put("type", "3");
                }else{
                    stageMap.put("type", "4");
                }
                stageList.add(stageMap);
            }
        }
        return stageList;
    }
}
