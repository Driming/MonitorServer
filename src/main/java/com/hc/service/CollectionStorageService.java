package com.hc.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.hc.dao.CollectionStorageDao;
import com.hc.entity.storage.dataPercent.StorageDataPercentInfo;
import com.hc.util.MessageUtils;
import com.hc.util.map.StorageMap;
import com.hc.util.page.CommonPageListInfo;
import com.hc.util.page.PageErrorException;
import com.hc.util.page.template.PageObjInfo;
import com.hc.vo.StorageDataIndexDetailInfoVo;
import com.hc.vo.StorageDataIndexDetailInfoVo2;

@Service
public class CollectionStorageService {
	
	@Autowired
	private CollectionStorageDao collectionStorageDao;
	
	//获取存储数据百分比信息
	public Object findStorageDataPercentInfos(String id) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<StorageDataPercentInfo> collectInfos = new LinkedList<StorageDataPercentInfo>();
		List<StorageDataPercentInfo> typeInfos = new LinkedList<StorageDataPercentInfo>();
		List<StorageDataPercentInfo> infos = collectionStorageDao.findStorageDataPercentInfos(id);
		for(StorageDataPercentInfo info : infos){
			if(info.getType() == StorageMap.DATA_TYPE)
				typeInfos.add(info);
			else
				collectInfos.add(info);
		}
		resultMap.put("collectInfos", collectInfos);
		resultMap.put("typeInfos", typeInfos);
		return MessageUtils.returnSuccess(resultMap);
	}
	
	public Object findStorageDataSizeInfos(
			String id, Integer currentPage, Integer perPageSize) {
		List<StorageDataPercentInfo> infos = collectionStorageDao.findStorageDataSizeInfos(id);
		//分页
		try {
			CommonPageListInfo<StorageDataPercentInfo> commonPage = new CommonPageListInfo<>();
			return MessageUtils.returnSuccess(commonPage.page(infos, currentPage, perPageSize));
		} catch (PageErrorException e) {
			e.printStackTrace();
			return MessageUtils.pageOutOfRange();
		}
	}

	//获取存储数据字段对应的详细信息
	public Object findStorageDataIndexDetailInfos(String id, String collectName, String field, Short fieldType,
			int currentPage, int pageSize) {
		if(id == null
				|| collectName == null 
				|| field == null
				|| fieldType == null)
			return MessageUtils.parameterNullError();
		
		Page<StorageDataIndexDetailInfoVo> infoVos = collectionStorageDao.findStorageDataIndexDetailInfos(
				id, collectName, field, fieldType, currentPage, pageSize);
		StorageDataIndexDetailInfoVo2 infoVo2 = transferInfoVo2(infoVos);
		PageObjInfo<StorageDataIndexDetailInfoVo2> pageInfo = new PageObjInfo<StorageDataIndexDetailInfoVo2>();
		pageInfo.setCurrentPage(currentPage);
		pageInfo.setPerPageCount(pageSize);
		pageInfo.setTotalCount((int) infoVos.getTotal());
		pageInfo.setPageCount(infoVos.getPages());
		pageInfo.setObj(infoVo2);

		if (currentPage <= 0 || (currentPage > infoVos.getPages() && currentPage != 1))
			return MessageUtils.requestPageError();
		return MessageUtils.returnSuccess(pageInfo);
	}
	
	//通过字段包含关系，反向获取存储数据字段的详细信息
	public Object findStorageDataIndexDetailInfosByContainField(String id, String collectName, String field,
			int currentPage, int pageSize) {
		if(id == null
				|| collectName == null 
				|| field == null)
			return MessageUtils.parameterNullError();
		
		Short[] filedTypes = {2, 3};
		Page<StorageDataIndexDetailInfoVo> infoVos = collectionStorageDao.findStorageDataIndexDetailInfosByContainField(
				id, collectName, Arrays.asList(filedTypes), field, currentPage, pageSize);
		StorageDataIndexDetailInfoVo2 infoVo2 = transferInfoVo2(infoVos);
		PageObjInfo<StorageDataIndexDetailInfoVo2> pageInfo = new PageObjInfo<StorageDataIndexDetailInfoVo2>();
		pageInfo.setCurrentPage(currentPage);
		pageInfo.setPerPageCount(pageSize);
		pageInfo.setTotalCount((int) infoVos.getTotal());
		pageInfo.setPageCount(infoVos.getPages());
		pageInfo.setObj(infoVo2);

		if (currentPage <= 0 || (currentPage > infoVos.getPages() && currentPage != 1))
			return MessageUtils.requestPageError();
		return MessageUtils.returnSuccess(pageInfo);
	}

	private StorageDataIndexDetailInfoVo2 transferInfoVo2(Page<StorageDataIndexDetailInfoVo> infoVos) {
		StorageDataIndexDetailInfoVo2 infoVo2 = new StorageDataIndexDetailInfoVo2();
		List<String> elements = addUpElements(infoVos);
		List<List<String>> values = new LinkedList<List<String>>();
		List<List<String>> fields = new LinkedList<List<String>>();
		List<Long> times = new LinkedList<Long>();
		
		for(StorageDataIndexDetailInfoVo infoVo : infoVos){
			List<String> value = new LinkedList<String>();
			List<String> existElement = infoVo.getElements();
			List<String> existValue = infoVo.getValues();
			for(String element : elements){
				int index = existElement.indexOf(element);
				if(index != -1)
					value.add(existValue.get(index));
				else
					value.add(null);
			}
			values.add(value);
			fields.add(infoVo.getFields());
			times.add(infoVo.getTime());
		}
		infoVo2.setElements(elements);
		infoVo2.setValues(values);
		infoVo2.setFields(fields);
		infoVo2.setTimes(times);
		return infoVo2;
	}

	private List<String> addUpElements(Page<StorageDataIndexDetailInfoVo> infoVos) {
		List<String> elements = new ArrayList<String>();
		for(StorageDataIndexDetailInfoVo infoVo : infoVos){
			List<String> existElements = infoVo.getElements();
			if(existElements != null)
				if(!elements.containsAll(existElements)){
					for(String element : existElements)
						if(!elements.contains(element))
							elements.add(element);
				}
		}
		return elements;
	}

}
