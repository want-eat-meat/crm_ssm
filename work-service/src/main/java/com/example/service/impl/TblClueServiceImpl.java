package com.example.service.impl;

import com.example.enums.ResultEnum;
import com.example.exception.ResultException;
import com.example.mapper.TblClueMapper;
import com.example.mapper.TblClueRemarkMapper;
import com.example.mapper.TblDicValueMapper;
import com.example.mapper.TblUserMapper;
import com.example.pojo.*;
import com.example.service.TblClueService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.utils.*;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TblClueServiceImpl implements TblClueService {
    @Autowired
    private TblClueMapper clueMapper;
    @Autowired
    private TblUserMapper userMapper;
    @Autowired
    private TblClueRemarkMapper remarkMapper;

    @Override
    public List<TblUser> listUsers() {
        try{
            List<TblUser> tblUsers = userMapper.selectByExample(null);
            if(tblUsers.size() == 0){
                throw new ResultException(ResultEnum.SUCCESS_NODATA);
            }
            return tblUsers;
        }catch (Exception e){
            throw new ResultException(ResultEnum.FAIL);
        }
    }

    @Override
    public void add(TblClue clue) {
        //获取id
        clue.setId(UUIDUtil.getUUID());
        //获取创建时间
        clue.setCreatetime(DateTimeUtil.getSysTime());
        try{
            clueMapper.insert(clue);
        }catch (ResultException e){
            throw new ResultException(ResultEnum.FAIL);
        }
    }

    @Override
    public PageResult list(int start, int count, String fullname, String company, String phone, String source, String owner, String mphone, String state) {
        TblClueExample example = new TblClueExample();
        TblClueExample.Criteria criteria = example.createCriteria();
        //条件判断
        if(fullname != null && !"".equals(fullname)){
            criteria.andFullnameLike("%" +fullname+ "%");
        }
        if(company != null && !"".equals(company)){
            criteria.andCompanyLike("%" +company+ "%");
        }
        if(phone != null && !"".equals(phone)){
            criteria.andPhoneLike("%" +phone+ "%");
        }
        if(source != null && !"".equals(source)){
            criteria.andSourceEqualTo(source);
        }
        if(owner != null && !"".equals(owner)){
            TblUserExample userExample = new TblUserExample();
            userExample.createCriteria().andNameLike("%" + owner +"%");
            List<TblUser> users = userMapper.selectByExample(userExample);
            List<String> ids =new ArrayList<>();
            for(TblUser user : users){
                ids.add(user.getId());
            }
            if(ids.size() != 0){
                criteria.andOwnerIn(ids);
            }
        }
        if(mphone != null && !"".equals(mphone)){
            criteria.andMphoneLike("%" +mphone+ "%");
        }
        if(state != null && !"".equals(state)){
            criteria.andStateEqualTo(state);
        }
        //查询
        try {
            //分页拦截
            PageHelper.startPage(start, count);
            List<TblClue> clues = clueMapper.selectByExample(example);
            //添加信息
            for (TblClue clue : clues) {
                TblUser user = userMapper.selectByPrimaryKey(clue.getOwner());
                clue.setOwnername(user.getName());
            }
            //分页信息
            PageInfo pageInfo = new PageInfo(clues);
            return new PageResult(pageInfo.getTotal(), pageInfo.getList());
        }catch (Exception e){
            throw new ResultException(ResultEnum.FAIL);
        }
    }

    @Override
    public void delete(List<String> ids) {
        TblClueExample example = new TblClueExample();
        example.createCriteria().andIdIn(ids);
        TblClueRemarkExample remarkExample = new TblClueRemarkExample();
        remarkExample.createCriteria().andClueidIn(ids);
        try{
            //删除备注
            remarkMapper.deleteByExample(remarkExample);
            //删除线索
            clueMapper.deleteByExample(example);
        }catch (Exception e){
            throw new ResultException(ResultEnum.FAIL);
        }
    }

    @Override
    public TblClue getById(String id) {
        try{
            TblClue clue = clueMapper.selectByPrimaryKey(id);
            if(clue == null){
                throw new ResultException(ResultEnum.SUCCESS_NODATA);
            }
            clue.setOwnername(userMapper.selectByPrimaryKey(clue.getOwner()).getName());
            clue.setCreatename(userMapper.selectByPrimaryKey(clue.getCreateby()).getName());
            if(clue.getEditby() != null && !"".equals(clue.getEditby())){
                clue.setEditname(userMapper.selectByPrimaryKey(clue.getEditby()).getName());
            }
            return clue;
        }catch (Exception e){
            throw new ResultException(ResultEnum.FAIL);
        }
    }

    @Override
    public void update(TblClue clue) {
        //设置修改时间
        clue.setEdittime(DateTimeUtil.getSysTime());

        //修改
        try{
            clueMapper.updateByPrimaryKeySelective(clue);
        }catch (Exception e){
            throw new ResultException(ResultEnum.FAIL);
        }
    }
}
