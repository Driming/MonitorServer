package com.hc.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hc.entity.index.error.ErrorInfoRecord;
import com.hc.entity.index.error.ErrorInfoRecordMapper;

@Repository
public class CollectionIndexDao {

	@Autowired
	private ErrorInfoRecordMapper errorInfoRecordMapper;

	public Page<ErrorInfoRecord> findErrorInfosByPage(int currentPage, int pageSize) {
		PageHelper.startPage(currentPage, pageSize);
		return errorInfoRecordMapper.selectAll();
	}
	
	public int addErrorInfos(List<ErrorInfoRecord> records){
		return errorInfoRecordMapper.insertBatch(records);
	}
	
}
