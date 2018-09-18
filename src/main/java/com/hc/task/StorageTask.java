package com.hc.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hc.dataStorage.client.DataStorageClient;
import com.hc.dataStorage.storage.record.StorageRecord;
import com.hc.entity.collection.server.CollectionServer;
import com.hc.registry.ServiceClient;
import com.hc.registry.entry.ServiceInfo;
import com.hc.service.CollectionMonitorService;
import com.hc.service.StorageTaskService;
import com.hc.util.StringUtils;
import com.hc.util.map.ServerMap;
import com.hc.util.properties.PropertiesBuilder;

@Component
public class StorageTask {

	@Autowired
	private StorageTaskService StorageTaskService;
	
	@Autowired
	private CollectionMonitorService collectionMonitorService;

	@Scheduled(cron = "0 * * * * ? ")
	public void register() {
		int isUsedCs = 1;
		short type = ServerMap.STORAGE_SERVER;
		String serviceflag = PropertiesBuilder.buildProperties().getFlags()[1];
		int serviceManagerPort = PropertiesBuilder.buildProperties().getPort();
		String serviceManagerAddress = PropertiesBuilder.buildProperties().getIp();
		ServiceClient serviceClient = new ServiceClient(serviceManagerAddress, serviceManagerPort, serviceflag);
		List<ServiceInfo> serviceInfos = serviceClient.getServiceInfo(serviceflag);
		List<CollectionServer> storageServers = new ArrayList<CollectionServer>();
 
		
		for (ServiceInfo serverInfo : serviceInfos) {
			CollectionServer storageServer = new CollectionServer();
			String host = serverInfo.getHost();
			int port = serverInfo.getPort();
			String server = StringUtils.join(host, ":", port);
			storageServer.setName(String.format("%s存储", host.split("\\.")[host.split("\\.").length - 1]));
			storageServer.setCsid(server);
			storageServer.setServer(server);
			storageServer.setFlag(serverInfo.getFlag());
			storageServer.setType(ServerMap.STORAGE_SERVER);
			storageServer.setVersion(String.valueOf(serverInfo.getVersion()));
			storageServers.add(storageServer);
		}
		collectionMonitorService.addCollectionServers(storageServers, isUsedCs, type);

		for (ServiceInfo serviceInfo : serviceInfos) {
			try {
				String host = serviceInfo.getHost();
				int port = serviceInfo.getPort();
				String id = StringUtils.join(host, ":", port);
				DataStorageClient client = new DataStorageClient(host, port);
				registerStorageSpace(id, client);
				registerStorageDataPercent(id, client);
				registerStorageDataField(id, client);
				registerStorageDataFieldDetail(id, client);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void registerStorageSpace(String id, DataStorageClient client) {
		@SuppressWarnings("rawtypes")
		StorageRecord record = client.getStorageUsage();
		Map<String, Object> result = record.getIndexes().indexes;

		StorageTaskService.registerStorageSpace(id, result);
	}

	private void registerStorageDataPercent(String id, DataStorageClient client) {
		@SuppressWarnings("rawtypes")
		StorageRecord record = client.getAllStoragePercentStats();
		Map<String, Object> result = record.getIndexes().indexes;

		StorageTaskService.registerStorageDataPercent(id, result);
	}

	private void registerStorageDataField(String id, DataStorageClient client) {
		@SuppressWarnings("rawtypes")
		StorageRecord record = client.getAllFieldCountStats();
		Map<String, Object> result = record.getIndexes().indexes;

		StorageTaskService.registerStorageDataField(id, result);
	}

	private void registerStorageDataFieldDetail(String id, DataStorageClient client) {
		StorageTaskService.registerStorageDataFieldDetail(id, client);
	}

}
