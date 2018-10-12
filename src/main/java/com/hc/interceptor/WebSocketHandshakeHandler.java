package com.hc.interceptor;

import java.util.List;
import java.util.Map;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.hc.dao.CollectionMonitorDao;
import com.hc.entity.collection.server.CollectionServer;
import com.hc.service.WebSocketSendService;
import com.hc.util.context.ContextCore;
import com.hc.util.map.ServerMap;

/**
 * WebSocket 握手信息
 *
 * @ClassName: WebSocketHandshakeHandler.java
 * @Description: WebSocket 握手信息
 */
public class WebSocketHandshakeHandler extends TextWebSocketHandler{

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception{
		super.afterConnectionClosed(session, status);
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception{
		String name = session.getPrincipal().getName();

		int isUsedCs = 1;
		long startTime = 0;
		short type = ServerMap.COLLECT_SERVER;
		CollectionMonitorDao collectionMonitorDao = (CollectionMonitorDao) ContextCore.getBean(CollectionMonitorDao.class);
		WebSocketSendService webSocketSendService = (WebSocketSendService) ContextCore.getBean(WebSocketSendService.class);
		Map<String, Object> serverMaxTimes = collectionMonitorDao.selectCollectionHistoryServerMaxTime();
		List<CollectionServer> collectionServers = collectionMonitorDao.findCollectionServers(isUsedCs, type);
		for(CollectionServer collectionServer : collectionServers){
			String csid = collectionServer.getCsid();
			Object time = serverMaxTimes.get(csid);
			if(! csid.equals(name)){
				continue;
			}
			if(time == null){
				webSocketSendService.synchronizeTaskHistory(csid, startTime);
			}else{
				webSocketSendService.synchronizeTaskHistory(csid, Long.parseLong(time.toString()));
			}

		}
		super.afterConnectionEstablished(session);
	}

	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception{
		super.handleMessage(session, message);
	}
}  