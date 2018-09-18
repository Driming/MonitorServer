package com.hc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hc.dao.CollectionMonitorDao;
import com.hc.dao.CollectionStorageDao;
import com.hc.service.CollectionStorageService;
import com.hc.util.MessageUtils;
import com.hc.util.map.ServerMap;

@Controller
@RequestMapping("/collect/storage")
public class CollectionStorageController {
	
	@Autowired
	private CollectionStorageService collectionStorageService;
	
	@Autowired
	private CollectionStorageDao collectionStorageDao;
	
	@Autowired
	private CollectionMonitorDao collectionMonitorDao;
	
	@ResponseBody
	@RequestMapping("/servers")
	public Object getStorageServers(){
		int isUsedCs = 1;
		short type = ServerMap.STORAGE_SERVER;
		return MessageUtils.returnSuccess(collectionMonitorDao.findCollectionServers(isUsedCs, type));
	}
	
	@ResponseBody
	@RequestMapping("/space/info")
	public Object getStorageSpaceInfos(String id){
		return MessageUtils.returnSuccess(collectionStorageDao.findStorageSpaceInfos(id));
	}
	
	@ResponseBody
	@RequestMapping("/data/percent/info")
	public Object getStorageDataPercentInfos(String id){
		return collectionStorageService.findStorageDataPercentInfos(id);
	}
	
	@ResponseBody
	@RequestMapping("/data/collect/size/info")
	public Object getStorageDataSizeInfos(String id,
			@RequestParam(defaultValue = "1")Integer currentPage,
			@RequestParam(defaultValue = "10")Integer perPageSize){
		return collectionStorageService.findStorageDataSizeInfos(id, currentPage, perPageSize);
	}
	
	@ResponseBody
	@RequestMapping("/data/field/info")
	public Object getStorageDataFieldInfos(String id){
		return MessageUtils.returnSuccess(collectionStorageDao.findStorageDataFieldInfos(id));
	}
	
	@ResponseBody
	@RequestMapping("/data/field/detail/info")
	public Object getStorageDataFieldDetailInfos(String id, String collectName){
		return MessageUtils.returnSuccess(collectionStorageDao.findStorageDataFieldDetailInfos(id, collectName));
	}
	
	@ResponseBody
	@RequestMapping("/data/index/detail/info")
	public Object getStorageDataIndexDetailInfos(String id,
				String collectName, String field,
				Short fieldType, int currentPage,
				int pageSize){
		return collectionStorageService.findStorageDataIndexDetailInfos(
				id, collectName, field, fieldType, currentPage, pageSize);
	}
	
	@ResponseBody
	@RequestMapping("/data/index/detail/info/byContainField")
	public Object getStorageDataIndexDetailInfosByContainField(String id,
			String collectName, String field,
			int currentPage, int pageSize){
		return collectionStorageService.findStorageDataIndexDetailInfosByContainField(
				id, collectName, field, currentPage, pageSize);
	}
}
