package com.hc.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hc.entity.collection.task.CollectionTask;
import com.hc.entity.control.user.User;
import com.hc.service.CollectionIndexService;
import com.hc.service.WebSocketSendService;
import com.hc.vo.IndexInfoVo;

@Component
public class CollectTask {
	
	@Autowired
	private WebSocketSendService webSocketSendService;
	
	@Autowired
	private CollectionIndexService collectionIndexService;

	@Scheduled(cron = "0 0 * * * ? ")
	public void sendIndexInfos(){
		int isUsedCs = 1;
		IndexInfoVo serverInfos = collectionIndexService.findIndexInfoVo(isUsedCs);
		webSocketSendService.indexServerInfo(serverInfos);
	}

	
    //@Scheduled(cron = "*/5 * * * * ? ")
	public void test(){
		CollectionTask ct = new CollectionTask();
		ct.setConfig("'{'driver':{'driver':'wave_weather','name':'WaveWeatherDriver','version':'1.0'},'reader':{'path':'http://172.22.1.175/di/http.action?userId=gmcrzh&pwd=zhuh123&interfaceId=getRACOcenfs&dataFormat=json&ymdhms={date:yyyyMMddHHmmss}'},'storage':{'database_prefix':'wave_weather','port':'8920','host':'10.148.83.5'},'task':{'delay':'2h','retry_times':'10','min_save_count':'1','work_threads':'1','name':'wave_weather','hours':'3','interval':'24h','min_read_count':'1','retry_interval':'10m'}}'");
		ct.setCtid("1433");
		ct.setCsid("test1");
		User user = new User();
		user.setUsername("admin");
		user.setPassword("admin");
		webSocketSendService.updateTaskConfigure(2342344, ct);
		//collectionMonitorService.updateTaskConfigure(ct, user);
	}
    
}
