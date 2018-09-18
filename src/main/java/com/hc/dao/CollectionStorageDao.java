package com.hc.dao;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hc.entity.storage.dataField.StorageDataFieldInfo;
import com.hc.entity.storage.dataField.StorageDataFieldInfoMapper;
import com.hc.entity.storage.dataField.fieldDetail.StorageDataFieldDetailInfo;
import com.hc.entity.storage.dataField.fieldDetail.StorageDataFieldDetailInfoMapper;
import com.hc.entity.storage.dataField.indexDetail.StorageDataIndexDetailInfo;
import com.hc.entity.storage.dataField.indexDetail.StorageDataIndexDetailInfoMapper;
import com.hc.entity.storage.dataPercent.StorageDataPercentInfo;
import com.hc.entity.storage.dataPercent.StorageDataPercentInfoMapper;
import com.hc.entity.storage.space.StorageSpaceInfo;
import com.hc.entity.storage.space.StorageSpaceInfoMapper;
import com.hc.vo.StorageDataIndexDetailInfoVo;

@Repository
public class CollectionStorageDao {
	@Autowired
	private StorageSpaceInfoMapper storageSpaceInfoMapper;
	
	@Autowired
	private StorageDataPercentInfoMapper storageDataPercentInfoMapper;
	
	@Autowired
	private StorageDataFieldInfoMapper storageDataFieldInfoMapper;
	
	@Autowired
	private StorageDataFieldDetailInfoMapper storageDataFieldDetailInfoMapper;
	
	@Autowired
	private StorageDataIndexDetailInfoMapper storageDataIndexDetailInfoMapper;
	
	
	//存储空间
	public List<StorageSpaceInfo> findStorageSpaceInfos(String id){
		return storageSpaceInfoMapper.selectStorageSpaceInfos(id);
	}
	
	public int addStorageSpaceInfoSelective(StorageSpaceInfo info){
		return storageSpaceInfoMapper.insertStorageSpaceInfoSelective(info);
	}
	
	public int updateStorageSpaceInfoSelective(StorageSpaceInfo info){
		return storageSpaceInfoMapper.updateStorageSpaceInfoSelective(info);
	}
	
	//数据比例信息
	public List<StorageDataPercentInfo> findStorageDataPercentInfos(String id){
		return storageDataPercentInfoMapper.selectTotal(id);
	}
	
	public int deleteStorageDataPercentInfoById(String id){
		return storageDataPercentInfoMapper.deleteById(id);
	}
	
	public int addStorageDatePercentInfoBatch(List<StorageDataPercentInfo> infos){
		return storageDataPercentInfoMapper.insertBatch(infos);
	}
	
	//数据容量信息
	public List<StorageDataPercentInfo> findStorageDataSizeInfos(String id) {
		return storageDataPercentInfoMapper.selectSizeInfos(id);
	}
	
	//数据字段统计信息
	public List<StorageDataFieldInfo> findStorageDataFieldInfos(String id){
		return storageDataFieldInfoMapper.selectAllById(id);
	}
	
	public int deleteStorageDataFieldInfoById(String id){
		return storageDataFieldInfoMapper.deleteById(id);
	}
	
	public int addStorageDataFieldInfoBatch(List<StorageDataFieldInfo> infos){
		return storageDataFieldInfoMapper.insertBatch(infos);
	}

	public List<StorageDataFieldInfo> findStorageDataFileCollectNames(String id) {
		return storageDataFieldInfoMapper.selectCollectNames(id);
	}

	//集合字段统计详细信息
	public int deleteStorageDataFieldDetailInfos(String id){
		return storageDataFieldDetailInfoMapper.deleteByid(id);
	}
	
	public int addStorageDataFieldDetailInfos(List<StorageDataFieldDetailInfo> infos){
		return storageDataFieldDetailInfoMapper.insertBatch(infos);
	}
	
	public int deleteStorageDataIndexDetailInfos(String id){
		return storageDataIndexDetailInfoMapper.deleteById(id);
	}
	
	public int addStorageDataIndexDetailInfos(List<StorageDataIndexDetailInfo> infos){
		return storageDataIndexDetailInfoMapper.insertBatch(infos);
	}
	
	public List<StorageDataFieldDetailInfo> findStorageDataFieldDetailInfos(String id, String collectName){
		return storageDataFieldDetailInfoMapper.selectById(id, collectName);
	}
	
	public Page<StorageDataIndexDetailInfoVo> findStorageDataIndexDetailInfos(String id, String collectName, String field,
			Short fieldType, int currentPage, int pageSize) {
		PageHelper.startPage(currentPage, pageSize);
		return storageDataIndexDetailInfoMapper.selectAll(
				id, collectName, field, Collections.singletonList(fieldType), null);
	}

	public Page<StorageDataIndexDetailInfoVo> findStorageDataIndexDetailInfosByContainField(String id,
			String collectName, List<Short> fieldTypes, String fieldsItem, int currentPage, int pageSize) {
		PageHelper.startPage(currentPage, pageSize);
		return storageDataIndexDetailInfoMapper.selectAll(
				id, collectName, null, fieldTypes, fieldsItem);
	}

}
