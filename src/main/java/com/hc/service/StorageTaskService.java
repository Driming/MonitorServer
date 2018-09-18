package com.hc.service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hc.dao.CollectionStorageDao;
import com.hc.dataStorage.client.DataStorageClient;
import com.hc.dataStorage.storage.record.StorageRecord;
import com.hc.dataobject.DataList;
import com.hc.dataobject.DataObject;
import com.hc.entity.storage.dataField.StorageDataFieldInfo;
import com.hc.entity.storage.dataField.fieldDetail.StorageDataFieldDetailInfo;
import com.hc.entity.storage.dataField.indexDetail.StorageDataIndexDetailInfo;
import com.hc.entity.storage.dataPercent.StorageDataPercentInfo;
import com.hc.entity.storage.space.StorageSpaceInfo;
import com.hc.util.map.StorageMap;

@Service
public class StorageTaskService {
	
	@Autowired
	private CollectionStorageDao collectionStorageDao;

	
	public void registerStorageSpace(String id, Map<String, Object> result) {
		DataObject dataObject = (DataObject) result.get("stats");
		StorageSpaceInfo info = new StorageSpaceInfo();
		info.setId(id);
		info.setTotalspace(dataObject.getDouble("totalspace"));
		info.setDatacount(dataObject.getDouble("datacount"));
		info.setDbspace(dataObject.getDouble("dbspace"));
		info.setPercentdbspace(dataObject.getDouble("percentdbspace"));
		info.setOtherspace(dataObject.getDouble("otherspace"));
		info.setPercentotherspace(dataObject.getDouble("percentotherspace"));
		info.setFreespace(dataObject.getDouble("freespace"));
		info.setPercentfreespace(dataObject.getDouble("percentfreespace"));

		List<StorageSpaceInfo> infos = collectionStorageDao.findStorageSpaceInfos(id);
		if (infos == null || infos.isEmpty())
			collectionStorageDao.addStorageSpaceInfoSelective(info);
		else
			collectionStorageDao.updateStorageSpaceInfoSelective(info);
	}
	
	public void registerStorageDataPercent(String id, Map<String, Object> result) {
		List<StorageDataPercentInfo> infos = new LinkedList<StorageDataPercentInfo>();
		DataList dataList = (DataList) result.get("collect_stats");
		for (int i = 0; i < dataList.size(); i++) {
			DataObject dataObject = (DataObject) dataList.get(i);
			StorageDataPercentInfo info = new StorageDataPercentInfo();
			info.setType(StorageMap.COLLECT_TYPE);
			info.setId(id);
			info.setCname(dataObject.getString("cname"));
			info.setTypename(dataObject.getString("data_type"));
			info.setName(dataObject.getString("collect_name"));
			info.setCollectsize(dataObject.getDouble("collectsize"));
			info.setDatacount(dataObject.getDouble("datacount"));
			info.setPercentcount(dataObject.getDouble("percentcount"));
			info.setPercentsize(dataObject.getDouble("percentsize"));
			infos.add(info);
		}

		dataList = (DataList) result.get("type_stats");
		for (int i = 0; i < dataList.size(); i++) {
			DataObject dataObject = (DataObject) dataList.get(i);
			StorageDataPercentInfo info = new StorageDataPercentInfo();
			info.setType(StorageMap.DATA_TYPE);
			info.setId(id);
			info.setName(dataObject.getString("type"));
			info.setCollectsize(dataObject.getDouble("collectsize"));
			info.setDatacount(dataObject.getDouble("datacount"));
			info.setPercentcount(dataObject.getDouble("percentcount"));
			info.setPercentsize(dataObject.getDouble("percentsize"));
			infos.add(info);
		}

		if(!infos.isEmpty()){
			collectionStorageDao.deleteStorageDataPercentInfoById(id);
			collectionStorageDao.addStorageDatePercentInfoBatch(infos);
		}
	}
	
	public void registerStorageDataField(String id, Map<String, Object> result) {
		List<StorageDataFieldInfo> infos = new LinkedList<StorageDataFieldInfo>();
		DataList dataList = (DataList) result.get("field_count_stats");
		for (int i = 0; i < dataList.size(); i++) {
			DataObject dataObject = (DataObject) dataList.get(i);
			StorageDataFieldInfo info = new StorageDataFieldInfo();
			Date datetime = dataObject.getDate("datetime");
			info.setId(id);
			info.setCname(dataObject.getString("cname"));
			info.setCollectname(dataObject.getString("collect_name"));
			info.setCollectsize(dataObject.getDouble("collectsize"));
			info.setDatacount(dataObject.getDouble("datacount"));
			info.setDatasize(dataObject.getDouble("datasize"));
			info.setIndexsize(dataObject.getDouble("indexsize"));
			info.setStoragesize(dataObject.getDouble("storagesize"));
			info.setAvgobjsize(dataObject.getDouble("avgobjsize"));
			info.setDatetime(datetime != null ? datetime.getTime() : null);
			info.setExpectday(dataObject.getDouble("expectday"));
			info.setExpectgrowth(dataObject.getDouble("expectgrowth"));
			info.setPercentcount(dataObject.getDouble("percentcount"));
			info.setPercentsize(dataObject.getDouble("percentsize"));
			info.setDatatype(dataObject.getString("data_type"));
			infos.add(info);
		}

		if(!infos.isEmpty()){
			collectionStorageDao.deleteStorageDataFieldInfoById(id);
			collectionStorageDao.addStorageDataFieldInfoBatch(infos);
		}
	}
	
	public void registerStorageDataFieldDetail(String id, DataStorageClient client) {
		List<StorageDataFieldInfo> storageDataFieldInfos = collectionStorageDao.findStorageDataFileCollectNames(id);
		List<StorageDataFieldDetailInfo> fieldDetailInfos = new LinkedList<StorageDataFieldDetailInfo>();
		List<StorageDataIndexDetailInfo> indexDetailInfos = new LinkedList<StorageDataIndexDetailInfo>();
		for (StorageDataFieldInfo storageDataFieldInfo : storageDataFieldInfos) {
			StorageDataFieldDetailInfo fieldDetailInfo = new StorageDataFieldDetailInfo();
			StorageDataIndexDetailInfo indexDetailInfo = new StorageDataIndexDetailInfo();
			fieldDetailInfo.setId(id);
			fieldDetailInfo.setType(storageDataFieldInfo.getDatatype());
			fieldDetailInfo.setCollectname(storageDataFieldInfo.getCollectname());
			indexDetailInfo.setId(id);
			indexDetailInfo.setCollectname(storageDataFieldInfo.getCollectname());

			@SuppressWarnings("rawtypes")
			StorageRecord record = null;
			try{
				record = client.getFieldCountDetailStats(storageDataFieldInfo.getCollectname());
			}catch(Exception e){
				continue;
			}
			
			Map<String, Object> result = record.getIndexes().indexes;
			DataObject outDo = (DataObject) result.get("field_detail");
			DataList scalesList = (DataList) outDo.get("scales");
			DataList fieldsList = (DataList) outDo.get("fields");
			DataList indexesList = (DataList) outDo.get("indexes");
			if (scalesList != null) {
				fieldDetailInfo.setFieldtype(StorageMap.FIELD_TYPE_SCALES);
				indexDetailInfo.setFieldtype(StorageMap.FIELD_TYPE_SCALES);
				for (int i = 0; i < scalesList.size(); i++) {
					DataObject innerDo = (DataObject) scalesList.get(i);
					StorageDataFieldDetailInfo info = fieldDetailInfo.clone();
					info.setField(innerDo.getString("field"));
					info.setCount(innerDo.getDouble("count"));
					info.setPercent(innerDo.getDouble("percent"));
					fieldDetailInfos.add(info);

					@SuppressWarnings("rawtypes")
					StorageRecord storageRecord = client.getScaleFieldDetailStats(
							storageDataFieldInfo.getCollectname(), info.getField());
					registerDetail(indexDetailInfos, indexDetailInfo, storageRecord, info.getField());
				}
			}
			if (fieldsList != null) {
				fieldDetailInfo.setFieldtype(StorageMap.FIELD_TYPE_FIELDS);
				for (int j = 0; j < fieldsList.size(); j++) {
					DataObject innerDo = (DataObject) fieldsList.get(j);
					StorageDataFieldDetailInfo info = fieldDetailInfo.clone();
					info.setField(innerDo.getString("field"));
					info.setCount(innerDo.getDouble("count"));
					info.setPercent(innerDo.getDouble("percent"));
					fieldDetailInfos.add(info);
				}
			}
			if (indexesList != null) {
				fieldDetailInfo.setFieldtype(StorageMap.FIELD_TYPE_INDEXES);
				indexDetailInfo.setFieldtype(StorageMap.FIELD_TYPE_INDEXES);
				for (int k = 0; k < indexesList.size(); k++) {
					DataObject innerDo = (DataObject) indexesList.get(k);
					StorageDataFieldDetailInfo info = fieldDetailInfo.clone();
					info.setField(innerDo.getString("field"));
					info.setCount(innerDo.getDouble("count"));
					info.setPercent(innerDo.getDouble("percent"));
					fieldDetailInfos.add(info);

					@SuppressWarnings("rawtypes")
					StorageRecord storageRecord = client.getIndexFieldDetailStats(
							storageDataFieldInfo.getCollectname(), info.getField());
					registerDetail(indexDetailInfos, indexDetailInfo, storageRecord, info.getField());
				}
			}

		}

		if(!fieldDetailInfos.isEmpty()){
			collectionStorageDao.deleteStorageDataFieldDetailInfos(id);
			collectionStorageDao.addStorageDataFieldDetailInfos(fieldDetailInfos);
		}
		if(!indexDetailInfos.isEmpty()){
			collectionStorageDao.deleteStorageDataIndexDetailInfos(id);
			collectionStorageDao.addStorageDataIndexDetailInfos(indexDetailInfos);
		}
	}

	private void registerDetail(List<StorageDataIndexDetailInfo> indexDetailInfos,
			StorageDataIndexDetailInfo indexDetailInfo,
			@SuppressWarnings("rawtypes") StorageRecord record,
			String field) {		
		Map<String, Object> result = record.getIndexes().indexes;
		DataList indexList = (DataList) result.get("field_detail");
		for (int i = 0; i < indexList.size(); i++) {
			DataObject innerDo1 = (DataObject) indexList.get(i);
			indexDetailInfo.setField(field);
			indexDetailInfo.setElements(
					innerDo1.getDataList("indexes") == null ?
							innerDo1.getDataList("scales").toString() 
							: innerDo1.getDataList("indexes").toString());
			DataList innerDl = (DataList) innerDo1.getDataList("stats");
			for (int j = 0; j < innerDl.size(); j++) {
				DataObject innerDo2 = (DataObject) innerDl.get(j);
				Double time = innerDo2.getDouble("updatetime");
				StorageDataIndexDetailInfo info = indexDetailInfo.clone();
				info.setValues(innerDo2.getDataList("values").toString());
				info.setCount(innerDo2.getDouble("count"));
				info.setPercent(innerDo2.getDouble("percent"));
				info.setTime(time == null ? null : time.longValue());
				info.setFields(innerDo2.getDataList("datafields").toString());
				indexDetailInfos.add(info);
			}
		}		
	}

}
