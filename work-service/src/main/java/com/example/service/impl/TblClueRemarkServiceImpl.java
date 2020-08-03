package com.example.service.impl;

import com.example.enums.ResultEnum;
import com.example.mapper.*;
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
    private TblClueRemarkMapper clueRemarkMapper;
    @Autowired
    private TblUserMapper userMapper;
    @Autowired
    private TblActivityMapper activityMapper;
    @Autowired
    private TblClueActivityRelationMapper clueActivityRelationMapper;
    @Autowired
    private TblClueMapper clueMapper;
    @Autowired
    private TblCustomerMapper customerMapper;
    @Autowired
    private TblCustomerRemarkMapper customerRemarkMapper;
    @Autowired
    private TblContactsMapper contactsMapper;
    @Autowired
    private TblContactsRemarkMapper contactsRemarkMapper;
    @Autowired
    private TblContactsActivityRelationMapper contactsActivityRelationMapper;
    @Autowired
    private TblTranMapper tranMapper;
    @Autowired
    private TblTranHistoryMapper tranHistoryMapper;

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
            clueRemarkMapper.insertSelective(remark);
        }catch (Exception e){
            throw new ResultException(ResultEnum.FAIL);
        }
    }

    @Override
    public List<TblClueRemark> list(String id) {
        try{
            TblClueRemarkExample remarkExample = new TblClueRemarkExample();
            remarkExample.createCriteria().andClueidEqualTo(id);
            List<TblClueRemark> remarks = clueRemarkMapper.selectByExample(remarkExample);
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
            clueRemarkMapper.updateByPrimaryKeySelective(remark);
        }catch (Exception e){
            throw new ResultException(ResultEnum.FAIL);
        }
    }

    @Override
    public void delete(String id) {
        try{
            clueRemarkMapper.deleteByPrimaryKey(id);
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
                clueActivityRelationMapper.insert(relation);
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
           List<TblClueActivityRelation> relations = clueActivityRelationMapper.selectByExample(relationExample);
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
            clueActivityRelationMapper.deleteByExample(relationExample);
        }catch (Exception e){
            throw new ResultException(ResultEnum.FAIL);
        }
    }

    @Override
    public PageResult listActsBySearch(int start, int count, String id, String search) {
        //根据clue的id查询关联表
        TblClueActivityRelationExample relationExample = new TblClueActivityRelationExample();

        relationExample.createCriteria().andClueidEqualTo(id);
        List<TblClueActivityRelation> relations = clueActivityRelationMapper.selectByExample(relationExample);
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

    @Override
    public void convert(String clueid, String money, String convertname, String expdate, String stage, String actid, String createflag, String createby) {
        String createTime = DateTimeUtil.getSysTime();
        //获取线索
        TblClue clue = clueMapper.selectByPrimaryKey(clueid);
        //获取线索备注
        TblClueRemarkExample clueRemarkExample = new TblClueRemarkExample();
        clueRemarkExample.createCriteria().andClueidEqualTo(clue.getId());
        List<TblClueRemark> clueRemarks = clueRemarkMapper.selectByExample(clueRemarkExample);
        //获取线索与市场活动关联
        TblClueActivityRelationExample clueActivityRelationExample = new TblClueActivityRelationExample();
        clueActivityRelationExample.createCriteria().andClueidEqualTo(clue.getId());
        List<TblClueActivityRelation> clueActivityRelations = clueActivityRelationMapper.selectByExample(clueActivityRelationExample);
        //创建客户
        TblCustomer customer = null;
            //判断客户是否存在
        TblCustomerExample customerExample = new TblCustomerExample();
        customerExample.createCriteria().andNameEqualTo(clue.getCompany());
        List<TblCustomer> customers = customerMapper.selectByExample(customerExample);
        if(customers.size() == 0) {
            //不存在则创建
            customer = new TblCustomer();
            customer.setId(UUIDUtil.getUUID());
            customer.setOwner(clue.getOwner());
            customer.setName(clue.getCompany());
            customer.setWebsite(clue.getWebsite());
            customer.setPhone(clue.getPhone());
            customer.setCreateby(createby);
            customer.setCreatetime(createTime);
            customer.setContactsummary(clue.getContactsummary());
            customer.setNextcontacttime(clue.getNextcontacttime());
            customer.setDescription(clue.getDescription());
            customer.setAddress(clue.getAddress());
            customerMapper.insertSelective(customer);
        }else{
            //存在则获取
            customer = customers.get(0);
        }
        //创建联系人
        TblContacts contacts = new TblContacts();
        contacts.setId(UUIDUtil.getUUID());
        contacts.setOwner(clue.getOwner());
        contacts.setSource(clue.getSource());
        contacts.setCustomerid(customer.getId());
        contacts.setFullname(clue.getFullname());
        contacts.setAppellation(clue.getAppellation());
        contacts.setEmail(clue.getEmail());
        contacts.setMphone(clue.getMphone());
        contacts.setJob(clue.getJob());
        contacts.setCreateby(createby);
        contacts.setCreatetime(createTime);
        contacts.setDescription(clue.getDescription());
        contacts.setContactsummary(clue.getContactsummary());
        contacts.setNextcontacttime(clue.getNextcontacttime());
        contacts.setAddress(clue.getAddress());
        contactsMapper.insertSelective(contacts);
        //判断是否创建交易
        if("1".equals(createflag)){
            //判断输入的信息
            if(money == null || "".equals(money)||
                    convertname == null || "".equals(convertname) ||
                    expdate == null || "".equals(expdate) ||
                    stage == null || "".equals(stage) ||
                    actid == null || "".equals(actid)){
                throw new ResultException("请完善信息！");
            }
            //创建交易
            TblTran tran = new TblTran();
            tran.setId(UUIDUtil.getUUID());
            tran.setOwner(clue.getOwner());
            tran.setMoney(money);
            tran.setName(convertname);
            tran.setExpecteddate(expdate);
            tran.setCustomerid(customer.getId());
            tran.setStage(stage);
            tran.setSource(clue.getSource());
            tran.setActivityid(actid);
            tran.setContactsid(contacts.getId());
            tran.setCreateby(createby);
            tran.setCreatetime(createTime);
            tran.setDescription(clue.getDescription());
            tran.setContactsummary(clue.getContactsummary());
            tran.setNextcontacttime(clue.getNextcontacttime());
            tranMapper.insertSelective(tran);
            //创建交易历史
            TblTranHistory tranHistory = new TblTranHistory();
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setStage(stage);
            tranHistory.setMoney(money);
            tranHistory.setExpecteddate(expdate);
            tranHistory.setCreateby(createby);
            tranHistory.setCreatetime(createTime);
            tranHistory.setTranid(tran.getId());
            tranHistoryMapper.insertSelective(tranHistory);
        }
        //创建客户和联系人备注
        for(TblClueRemark remark : clueRemarks){
            //创建客户备注
            TblCustomerRemark customerRemark = new TblCustomerRemark();
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setNotecontent(remark.getNotecontent());
            customerRemark.setCreateby(createby);
            customerRemark.setCreatetime(createTime);
            customerRemark.setCustomerid(customer.getId());
                //编辑标志 0：未编辑 1：编辑
            customerRemark.setEditflag("0");
            customerRemarkMapper.insertSelective(customerRemark);
            //创建联系人备注
            TblContactsRemark contactsRemark = new TblContactsRemark();
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setNotecontent(remark.getNotecontent());
            contactsRemark.setCreateby(createby);
            contactsRemark.setCreatetime(createTime);
            contactsRemark.setContactsid(contacts.getId());
                //编辑标志 0：未编辑 1：编辑
            contactsRemark.setEditflag("0");
            contactsRemarkMapper.insertSelective(contactsRemark);
        }
        //创建联系人与市场活动关联
        for(TblClueActivityRelation clueActivityRelation : clueActivityRelations){
            TblContactsActivityRelation contactsActivityRelation = new TblContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setContactsid(contacts.getId());
            contactsActivityRelation.setActivityid(clueActivityRelation.getActivityid());
            contactsActivityRelationMapper.insert(contactsActivityRelation);
        }
        //删除线索与市场活动关联
        TblClueActivityRelationExample clueActivityRelationExample1 = new TblClueActivityRelationExample();
        clueActivityRelationExample1.createCriteria().andClueidEqualTo(clue.getId());
        clueActivityRelationMapper.deleteByExample(clueActivityRelationExample1);
        //删除线索备注
        TblClueRemarkExample clueRemarkExample1 = new TblClueRemarkExample();
        clueRemarkExample1.createCriteria().andClueidEqualTo(clue.getId());
        clueRemarkMapper.deleteByExample(clueRemarkExample1);
        //删除线索
        clueMapper.deleteByPrimaryKey(clue.getId());
    }
}
