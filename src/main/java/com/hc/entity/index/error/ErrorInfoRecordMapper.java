package com.hc.entity.index.error;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;

public interface ErrorInfoRecordMapper {
    int insertBatch(@Param("records")List<ErrorInfoRecord> records);

    int insertSelective(ErrorInfoRecord record);

    Page<ErrorInfoRecord> selectAll();
}