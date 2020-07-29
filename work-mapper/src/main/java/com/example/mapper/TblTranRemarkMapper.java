package com.example.mapper;

import com.example.pojo.TblTranRemark;
import com.example.pojo.TblTranRemarkExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TblTranRemarkMapper {
    int countByExample(TblTranRemarkExample example);

    int deleteByExample(TblTranRemarkExample example);

    int deleteByPrimaryKey(String id);

    int insert(TblTranRemark record);

    int insertSelective(TblTranRemark record);

    List<TblTranRemark> selectByExample(TblTranRemarkExample example);

    TblTranRemark selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") TblTranRemark record, @Param("example") TblTranRemarkExample example);

    int updateByExample(@Param("record") TblTranRemark record, @Param("example") TblTranRemarkExample example);

    int updateByPrimaryKeySelective(TblTranRemark record);

    int updateByPrimaryKey(TblTranRemark record);
}