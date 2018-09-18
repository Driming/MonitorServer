package com.hc.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hc.dao.CollectionServiceDao;
import com.hc.entity.service.record.ServiceUrlUpdateRecord;
import com.hc.entity.service.urlDir.ServiceUrlDir;
import com.hc.entity.service.urlInfo.ServiceUrlInfo;
import com.hc.registry.entry.ServiceInfo;
import com.hc.util.StringUtils;
import com.hc.util.map.ServiceMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class ServiceTaskService {

	@Autowired
	private CollectionServiceDao collectionServiceDao;

	public void upsertServiceData(ServiceInfo serviceInfo) {
		int sequence = 0;
		List<ServiceUrlDir> urlDirs = new ArrayList<ServiceUrlDir>();
		List<ServiceUrlInfo> urlInfos = new ArrayList<ServiceUrlInfo>();
		String server = StringUtils.join(serviceInfo.getHost(), ":", serviceInfo.getPort());

		String message = serviceInfo.getDesc();
		if (message != null) {
			try {
				JSONArray messageArray = JSONArray.fromObject(message);
				parseMessage(urlDirs, urlInfos, messageArray, server, null, sequence);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		cudServiceUrlDir(Collections.singletonList(server), urlDirs);
		cudServiceUrlInfo(Collections.singletonList(server), urlInfos);
	}

	private void cudServiceUrlInfo(List<String> serverLists, List<ServiceUrlInfo> urlInfos) {
		List<ServiceUrlInfo> removeUrlInfos = new LinkedList<ServiceUrlInfo>();
		List<ServiceUrlInfo> insertUrlInfos = new LinkedList<ServiceUrlInfo>();
		List<ServiceUrlInfo> updateUrlInfos = new LinkedList<ServiceUrlInfo>();
		List<ServiceUrlInfo> existUrlInfos = collectionServiceDao.findServiceUrlInfos(serverLists);
		removeUrlInfos.addAll(existUrlInfos);
		if (urlInfos == null || urlInfos.isEmpty())
			return;
		for (int i = 0; i < urlInfos.size(); i++) {
			ServiceUrlInfo urlInfo = urlInfos.get(i);

			if (existUrlInfos.isEmpty())
				insertUrlInfos.add(urlInfo);
			for (ServiceUrlInfo existUrlInfo : existUrlInfos) {
				if (existUrlInfo.getServer().equals(urlInfo.getServer())
						&& existUrlInfo.getUrl().equals(urlInfo.getUrl())) {
					removeUrlInfos.remove(existUrlInfo);
					updateUrlInfos.add(urlInfo);
					break;
				}
				if (existUrlInfos.indexOf(existUrlInfo) == existUrlInfos.size() - 1)
					insertUrlInfos.add(urlInfo);
			}
		}

		addServiceUrlUpdateRecordByInsert(insertUrlInfos);
		addServiceUrlUpdateRecordByRemove(removeUrlInfos);
		if (!insertUrlInfos.isEmpty())
			collectionServiceDao.addServiceUrlInfoBatch(insertUrlInfos);
		if (!updateUrlInfos.isEmpty())
			collectionServiceDao.updateServiceUrlInfoBatch(updateUrlInfos);
		if (!removeUrlInfos.isEmpty())
			collectionServiceDao.removeServiceUrlInfoBatch(removeUrlInfos);
	}

	private void cudServiceUrlDir(List<String> serverLists, List<ServiceUrlDir> urlDirs) {
		List<ServiceUrlDir> removeUrlDirs = new LinkedList<ServiceUrlDir>();
		List<ServiceUrlDir> insertUrlDirs = new LinkedList<ServiceUrlDir>();
		List<ServiceUrlDir> updateUrlDirs = new LinkedList<ServiceUrlDir>();
		List<ServiceUrlDir> existUrlDirs = collectionServiceDao.findServiceUrlDirs(serverLists);
		removeUrlDirs.addAll(existUrlDirs);
		if (urlDirs == null || urlDirs.isEmpty())
			return;
		for (int i = 0; i < urlDirs.size(); i++) {
			ServiceUrlDir urlDir = urlDirs.get(i);

			if (existUrlDirs.isEmpty())
				insertUrlDirs.add(urlDir);
			for (ServiceUrlDir existUrlDir : existUrlDirs) {
				if (existUrlDir.getServer().equals(urlDir.getServer())
						&& existUrlDir.getUrl().equals(urlDir.getUrl())) {
					removeUrlDirs.remove(existUrlDir);
					updateUrlDirs.add(urlDir);
					break;
				}
				if (existUrlDirs.indexOf(existUrlDir) == existUrlDirs.size() - 1)
					insertUrlDirs.add(urlDir);
			}
		}
		if (!insertUrlDirs.isEmpty())
			collectionServiceDao.addServiceUrlDirBatch(insertUrlDirs);
		if (!updateUrlDirs.isEmpty())
			collectionServiceDao.updateServiceUrlDirBatch(updateUrlDirs);
		if (!removeUrlDirs.isEmpty())
			collectionServiceDao.removeServiceUrlDirBatch(removeUrlDirs);
	}

	private void parseMessage(List<ServiceUrlDir> urlDirs, List<ServiceUrlInfo> urlInfos, JSONArray messageArray,
			String server, String dirUrl, int sequence) {
		for (int i = 0; i < messageArray.size(); i++) {
			JSONObject subJson = JSONObject.fromObject(messageArray.get(i));
			if (!subJson.containsKey("dirType"))
				continue;

			ServiceUrlInfo urlInfo = new ServiceUrlInfo();
			String dirType = subJson.getString("dirType");
			urlInfo.setServer(server);
			urlInfo.setName(subJson.getString("name"));
			urlInfo.setUrl(subJson.getString("url"));
			urlInfo.setCname(subJson.getString("cname"));
			urlInfo.setDirtype(dirType);
			urlInfo.setSequence(++sequence);
			if (dirUrl == null)
				urlInfo.setIsroot((short) 1);
			else {
				urlInfo.setDirserver(server);
				urlInfo.setDirurl(dirUrl);
				urlInfo.setIsroot((short) 0);
			}

			switch (dirType) {
			case "group":
				urlDirs.add(urlInfo);
				JSONArray subArray = JSONArray.fromObject(subJson.getString("sub"));
				parseMessage(urlDirs, urlInfos, subArray, server, urlInfo.getUrl(), sequence);
				break;

			case "line":
				urlInfos.add(urlInfo);
				break;

			case "item":
				urlInfo.setReturntype(subJson.getString("return"));
				urlInfo.setDescription(subJson.getString("desc"));
				urlInfo.setArgs(subJson.getString("args"));
				urlInfos.add(urlInfo);
				break;

			default:
			}
		}

	}

	private void addServiceUrlUpdateRecordByInsert(List<ServiceUrlInfo> insertInfos) {
		try {
			List<ServiceUrlUpdateRecord> records = new ArrayList<ServiceUrlUpdateRecord>();
			for (ServiceUrlInfo insertInfo : insertInfos) {
				if (!"item".equals(insertInfo.getDirtype()))
					continue;

				ServiceUrlUpdateRecord record = new ServiceUrlUpdateRecord();
				record.setType(ServiceMap.ADD_RECORD);
				record.setDatetime(System.currentTimeMillis());
				String title = ServiceMap.titleAdd(insertInfo.getServer(), insertInfo.getCname());
				record.setTitle(title);
				String content = ServiceMap.contentAdd();
				record.setContent(content);
				String url = ServiceMap.getUrl(insertInfo.getServer(), insertInfo.getUrl());
				record.setUrl(url);
				records.add(record);
			}
			if (!records.isEmpty())
				collectionServiceDao.addServiceUrlUpdateReocrdBatch(records);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addServiceUrlUpdateRecordByRemove(List<ServiceUrlInfo> removeInfos) {
		try {
			List<ServiceUrlUpdateRecord> records = new ArrayList<ServiceUrlUpdateRecord>();
			for (ServiceUrlInfo removeInfo : removeInfos) {
				if (!"item".equals(removeInfo.getDirtype()))
					continue;

				ServiceUrlUpdateRecord record = new ServiceUrlUpdateRecord();
				record.setType(ServiceMap.REMOVE_RECORD);
				record.setDatetime(System.currentTimeMillis());
				String title = ServiceMap.titleRemove(removeInfo.getServer(), removeInfo.getCname());
				record.setTitle(title);
				String content = ServiceMap.contentRemove();
				record.setContent(content);
				String url = ServiceMap.getUrl(removeInfo.getServer(), removeInfo.getUrl());
				record.setUrl(url);
				records.add(record);
			}
			if (!records.isEmpty())
				collectionServiceDao.addServiceUrlUpdateReocrdBatch(records);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
