package com.example.mapper;

import com.example.pojo.TblClue;
import com.example.pojo.TblClueExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TblClueMapper {
    int countByExample(TblClueExample example);

    int deleteByExample(TblClueExample example);

    int deleteByPrimaryKey(String id);

    int insert(TblClue record);

    int insertSelective(TblClue record);

    List<TblClue> selectByExample(TblClueExample example);

    TblClue selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") TblClue record, @Param("example") TblClueExample example);

    int updateByExample(@Param("record") TblClue record, @Param("example") TblClueExample example);

    int updateByPrimaryKeySelective(TblClue record);

    int updateByPrimaryKey(TblClue record);

    List<Map<String, String>> clueState();
}