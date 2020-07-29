package com.example.mapper;

import com.example.pojo.TblTranHistory;
import com.example.pojo.TblTranHistoryExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TblTranHistoryMapper {
    int countByExample(TblTranHistoryExample example);

    int deleteByExample(TblTranHistoryExample example);

    int deleteByPrimaryKey(String id);

    int insert(TblTranHistory record);

    int insertSelective(TblTranHistory record);

    List<TblTranHistory> selectByExample(TblTranHistoryExample example);

    TblTranHistory selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") TblTranHistory record, @Param("example") TblTranHistoryExample example);

    int updateByExample(@Param("record") TblTranHistory record, @Param("example") TblTranHistoryExample example);

    int updateByPrimaryKeySelective(TblTranHistory record);

    int updateByPrimaryKey(TblTranHistory record);
}