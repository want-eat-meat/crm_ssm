package com.example.mapper;

import com.example.pojo.TblContactsRemark;
import com.example.pojo.TblContactsRemarkExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TblContactsRemarkMapper {
    int countByExample(TblContactsRemarkExample example);

    int deleteByExample(TblContactsRemarkExample example);

    int deleteByPrimaryKey(String id);

    int insert(TblContactsRemark record);

    int insertSelective(TblContactsRemark record);

    List<TblContactsRemark> selectByExample(TblContactsRemarkExample example);

    TblContactsRemark selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") TblContactsRemark record, @Param("example") TblContactsRemarkExample example);

    int updateByExample(@Param("record") TblContactsRemark record, @Param("example") TblContactsRemarkExample example);

    int updateByPrimaryKeySelective(TblContactsRemark record);

    int updateByPrimaryKey(TblContactsRemark record);
}