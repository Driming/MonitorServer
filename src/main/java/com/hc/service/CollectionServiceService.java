package com.hc.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hc.dao.CollectionMonitorDao;
import com.hc.dao.CollectionServiceDao;
import com.hc.entity.collection.server.CollectionServer;
import com.hc.entity.control.user.User;
import com.hc.entity.service.record.ServiceUrlUpdateRecord;
import com.hc.entity.service.urlDir.ServiceUrlDir;
import com.hc.entity.service.urlInfo.ServiceUrlInfo;
import com.hc.util.MessageUtils;
import com.hc.util.StringUtils;
import com.hc.util.map.ServiceMap;
import com.hc.vo.ServiceUrlVo;

@Service
public class CollectionServiceService {

	@Autowired
	private CollectionServiceDao collectionServiceDao;
	
	@Autowired
	private CollectionMonitorDao collectionMonitorDao;
	
	//返回单个服务器服务信息
	public Object findServiceUrls(String server) {
		List<ServiceUrlDir> urlDirs = collectionServiceDao.findServiceUrlDirs(server);
		List<ServiceUrlInfo> urlInfos = collectionServiceDao.findServiceUrlInfos(server);

		for (ServiceUrlInfo urlInfo : urlInfos) {
			if (urlInfo.getDirserver() != null && urlInfo.getDirurl() != null)
				combineUrlDirAndInfo(urlDirs, urlInfo);
			else
				urlDirs.add(urlInfo);
		}

		return MessageUtils.returnSuccess(urlDirs);
	}

	//返回所有服务器服务信息
	public Object findServiceUrls(Integer isUsedCs, Short type) {
		List<ServiceUrlVo> serviceUrlVos = new ArrayList<ServiceUrlVo>();

		List<CollectionServer> serviceServers = collectionMonitorDao.findCollectionServers(isUsedCs, type);
		for (CollectionServer serviceServer : serviceServers) {
			ServiceUrlVo serviceUrlVo = new ServiceUrlVo();
			String server = serviceServer.getServer();
			List<ServiceUrlDir> urlDirs = collectionServiceDao.findServiceUrlDirs(server);
			List<ServiceUrlInfo> urlInfos = collectionServiceDao.findServiceUrlInfos(server);

			for (ServiceUrlInfo urlInfo : urlInfos) {
				if (urlInfo.getDirserver() != null && urlInfo.getDirurl() != null)
					combineUrlDirAndInfo(urlDirs, urlInfo);
				else
					urlDirs.add(urlInfo);
			}
			
			serviceUrlVo.setServer(server);
			serviceUrlVo.setName(serviceServer.getName());
			serviceUrlVo.setDirtype("group");
			serviceUrlVo.setSubs(urlDirs);
			serviceUrlVos.add(serviceUrlVo);
		}

		return MessageUtils.returnSuccess(serviceUrlVos);
	}

	private void combineUrlDirAndInfo(List<ServiceUrlDir> urlDirs, ServiceUrlInfo urlInfo) {
		for (ServiceUrlDir urlDir : urlDirs) {
			if (urlDir.getServer().equals(urlInfo.getDirserver()) && urlDir.getUrl().equals(urlInfo.getDirurl())) {
				List<ServiceUrlDir> subs = urlDir.getSubs();
				if (subs == null) {
					subs = new LinkedList<ServiceUrlDir>();
					urlDir.setSubs(subs);
				}
				subs.add(urlInfo);
				return;
			}

			if (urlDir.getSubs() != null)
				combineUrlDirAndInfo(urlDir.getSubs(), urlInfo);
		}
	}

	public Object addServiceUrlUpdateRecord(ServiceUrlUpdateRecord record, User user) {
		if(user == null)
			return MessageUtils.permissionDeniedError();
		
		if(record.getType() == null)
			record.setType(ServiceMap.UPDATE_RECORD);
		record.setDatetime(System.currentTimeMillis());
		record.setAuthor(user.getUsername());
		int result = collectionServiceDao.addServiceUrlUpdateReocrdBatch(Collections.singletonList(record));
		if(result <= 0)
			return MessageUtils.operationFailedError();
		return MessageUtils.operationSuccess();
	}

	public Object updateServiceUrlUpdateRecord(ServiceUrlUpdateRecord record, User user) {
		if(user == null)
			return MessageUtils.permissionDeniedError();
		
		record.setDatetime(System.currentTimeMillis());
		record.setAuthor(user.getUsername());
		int result = collectionServiceDao.updateServiceUrlUpdateRecord(record);
		if(result <= 0)
			return MessageUtils.operationFailedError();
		return MessageUtils.operationSuccess();
	}

	public Object listServiceUrlUpdateRecord(String searchTime, ServiceUrlUpdateRecord record, int pageSize) {
		Integer year = null, month = null, day = null;
		if(searchTime != null){
			String[] times = searchTime.split("-");
			year = Integer.valueOf(times[0]);
			if(times.length >= 2)
				month = Integer.valueOf(times[1]);
			if(times.length >= 3)
				day = Integer.valueOf(times[2]);
		}
		if(record != null){
			if(record.getAuthor() != null)
				record.setAuthor(StringUtils.join("%", record.getAuthor(), "%"));
			if(record.getTitle() != null)
				record.setTitle(StringUtils.join("%", record.getTitle(), "%"));
			if(record.getUrl() != null)
				record.setUrl(StringUtils.join("%", record.getUrl(), "%"));
		}
		
		List<ServiceUrlUpdateRecord> records = collectionServiceDao.listServiceUrlUpdateReocrd(
				year, month, day, record, pageSize);
		return MessageUtils.returnSuccess(records);
	}

}
