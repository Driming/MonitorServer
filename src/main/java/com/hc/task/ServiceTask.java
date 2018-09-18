package com.hc.task;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.hc.dao.CollectionMonitorDao;
import com.hc.entity.collection.server.CollectionServer;
import com.hc.registry.ServiceClient;
import com.hc.registry.callback.CallBack;
import com.hc.registry.entry.ServiceInfo;
import com.hc.service.CollectionMonitorService;
import com.hc.service.ServiceTaskService;
import com.hc.util.StringUtils;
import com.hc.util.map.ServerMap;
import com.hc.util.properties.PropertiesBuilder;


@Component
public class ServiceTask {
 
	@Autowired
	private ServiceTaskService serviceTaskService;
	
	@Autowired
	private CollectionMonitorService collectionMonitorService;
	
	@Autowired
	private CollectionMonitorDao collectionMonitorDao;
	
	//private Logger logger = LoggerFactory.getLogger(ServiceTask.class);

	@Scheduled(cron = "0 */2 * * * ? ")
	public void checkHasNew() {
		String serviceflag = PropertiesBuilder.buildProperties().getFlags()[2];
		int serviceManagerPort = PropertiesBuilder.buildProperties().getPort();
		String serviceManagerAddress = PropertiesBuilder.buildProperties().getIp();
		ServiceClient client = new ServiceClient(serviceManagerAddress, serviceManagerPort);
		
		int isUsedCs = 1;
		short type = ServerMap.SERVICE_SERVER;
		List<ServiceInfo> serviceInfos = new ArrayList<>();
		List<CollectionServer> serviceServers = collectionMonitorDao.findCollectionServers(isUsedCs, type);
		
		for(CollectionServer serviceServer : serviceServers){
			String[] items = serviceServer.getServer().split(":");
			ServiceInfo serviceInfo = new ServiceInfo(serviceServer.getFlag(), items[0],
					Integer.valueOf(items[1]), Long.valueOf(serviceServer.getVersion()));
			serviceInfos.add(serviceInfo);
		}
	
		if(!serviceInfos.isEmpty()){
			client.checkVersion(serviceInfos, new CallBack<ServiceInfo>() {
				
				@Override
				public void onUpdate(ServiceInfo serviceInfo) {
					try {
						CollectionServer collectionServer = toCollectionServer(serviceInfo);
						collectionMonitorService.cudCollectionServers(null, Lists.newArrayList(collectionServer), null);
						serviceTaskService.upsertServiceData(serviceInfo);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				@Override
				public void onInsert(ServiceInfo serviceInfo) {
					try {
						CollectionServer collectionServer = toCollectionServer(serviceInfo);
						collectionMonitorService.cudCollectionServers(Lists.newArrayList(collectionServer), null, null);
						serviceTaskService.upsertServiceData(serviceInfo);
					} catch (Exception e) {
						e.printStackTrace();
					}			
				}
				
				@Override
				public void onDelete(ServiceInfo serviceInfo) {
					try {
						CollectionServer collectionServer = toCollectionServer(serviceInfo);
						collectionMonitorService.cudCollectionServers(null, null, Lists.newArrayList(collectionServer));
					} catch (Exception e) {
						e.printStackTrace();
					}				
				}
			});
		}else{
			serviceInfos = client.getServiceInfo(serviceflag);
			for(ServiceInfo serviceInfo : serviceInfos){
				CollectionServer collectionServer = toCollectionServer(serviceInfo);
				collectionMonitorService.cudCollectionServers(Lists.newArrayList(collectionServer), null, null);
				serviceTaskService.upsertServiceData(serviceInfo);
			}
		}
	}
	
	private CollectionServer toCollectionServer(ServiceInfo serviceInfo){
		short type = ServerMap.SERVICE_SERVER;
		CollectionServer serviceServer = new CollectionServer();
		String host = serviceInfo.getHost();
		int port = serviceInfo.getPort();
		String server = StringUtils.join(host, ":", port);
		serviceServer.setName(String.format("%s服务", host.split("\\.")[host.split("\\.").length - 1]));
		serviceServer.setServer(server);
		serviceServer.setFlag(serviceInfo.getFlag());
		serviceServer.setVersion(String.valueOf(serviceInfo.getVersion()));
		serviceServer.setCsid(server);
		serviceServer.setType(type);
		return serviceServer;
	}
}
