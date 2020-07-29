package com.example.mapper;

import com.example.pojo.TblClueActivityRelation;
import com.example.pojo.TblClueActivityRelationExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TblClueActivityRelationMapper {
    int countByExample(TblClueActivityRelationExample example);

    int deleteByExample(TblClueActivityRelationExample example);

    int deleteByPrimaryKey(String id);

    int insert(TblClueActivityRelation record);

    int insertSelective(TblClueActivityRelation record);

    List<TblClueActivityRelation> selectByExample(TblClueActivityRelationExample example);

    TblClueActivityRelation selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") TblClueActivityRelation record, @Param("example") TblClueActivityRelationExample example);

    int updateByExample(@Param("record") TblClueActivityRelation record, @Param("example") TblClueActivityRelationExample example);

    int updateByPrimaryKeySelective(TblClueActivityRelation record);

    int updateByPrimaryKey(TblClueActivityRelation record);
}