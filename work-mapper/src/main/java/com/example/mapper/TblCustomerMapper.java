package com.example.mapper;

import com.example.pojo.TblCustomer;
import com.example.pojo.TblCustomerExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TblCustomerMapper {
    int countByExample(TblCustomerExample example);

    int deleteByExample(TblCustomerExample example);

    int deleteByPrimaryKey(String id);

    int insert(TblCustomer record);

    int insertSelective(TblCustomer record);

    List<TblCustomer> selectByExample(TblCustomerExample example);

    TblCustomer selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") TblCustomer record, @Param("example") TblCustomerExample example);

    int updateByExample(@Param("record") TblCustomer record, @Param("example") TblCustomerExample example);

    int updateByPrimaryKeySelective(TblCustomer record);

    int updateByPrimaryKey(TblCustomer record);
}