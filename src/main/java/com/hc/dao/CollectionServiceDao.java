package com.hc.dao;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hc.entity.service.record.ServiceUrlUpdateRecord;
import com.hc.entity.service.record.ServiceUrlUpdateRecordMapper;
import com.hc.entity.service.urlDir.ServiceUrlDir;
import com.hc.entity.service.urlDir.ServiceUrlDirMapper;
import com.hc.entity.service.urlInfo.ServiceUrlInfo;
import com.hc.entity.service.urlInfo.ServiceUrlInfoMapper;

@Repository
public class CollectionServiceDao {
	
	@Autowired
	private ServiceUrlDirMapper serviceUrlDirMapper;
	
	@Autowired
	private ServiceUrlInfoMapper serviceUrlInfoMapper;
	
	@Autowired
	private ServiceUrlUpdateRecordMapper serviceUrlUpdateRecordMapper;

	
	public List<ServiceUrlInfo> findServiceUrlInfos(String server){
		return serviceUrlInfoMapper.selectAll(
				server != null ? Collections.singletonList(server) : null);
	}
	
	public List<ServiceUrlDir> findServiceUrlDirs(String server){
		return serviceUrlDirMapper.selectAllByRoot(
				server != null ? Collections.singletonList(server) : null);
	}
	
	public List<ServiceUrlInfo> findServiceUrlInfos(List<String> serverLists) {
		return serviceUrlInfoMapper.selectAll(serverLists);
	}

	public List<ServiceUrlDir> findServiceUrlDirs(List<String> serverLists) {
		return serviceUrlDirMapper.selectAll(serverLists);
	}
	
	public int addServiceUrlDirBatch(List<ServiceUrlDir> urlDirs){
		return serviceUrlDirMapper.insertBatch(urlDirs);
	}
	
	public int addServiceUrlInfoBatch(List<ServiceUrlInfo> urlInfos){
		return serviceUrlInfoMapper.insertBatch(urlInfos);
	}

	public int updateServiceUrlInfoBatch(List<ServiceUrlInfo> updateUrlInfos) {
		return serviceUrlInfoMapper.updateServiceUrlInfoBatch(updateUrlInfos);
	}

	public int removeServiceUrlInfoBatch(List<ServiceUrlInfo> removeUrlInfos) {
		return serviceUrlInfoMapper.deleteServiceUrlInfoBatch(removeUrlInfos);
	}

	public int updateServiceUrlDirBatch(List<ServiceUrlDir> updateUrlDirs) {
		return serviceUrlDirMapper.updateServiceUrlDirBatch(updateUrlDirs);
	}

	public int removeServiceUrlDirBatch(List<ServiceUrlDir> removeUrlDirs) {
		return serviceUrlDirMapper.deleteServiceUrlDirBatch(removeUrlDirs);
	}

	//url更新记录
	public int addServiceUrlUpdateReocrdBatch(List<ServiceUrlUpdateRecord> records) {
		return serviceUrlUpdateRecordMapper.insertBatch(records);
	}

	public int updateServiceUrlUpdateRecord(ServiceUrlUpdateRecord record) {
		return serviceUrlUpdateRecordMapper.updateOne(record);
	}

	public List<ServiceUrlUpdateRecord> listServiceUrlUpdateReocrd(Integer year, Integer month, Integer day,
			ServiceUrlUpdateRecord record, int pageSize) {
		return serviceUrlUpdateRecordMapper.selectAll(year, month, day, record, pageSize);
	}
	
}
