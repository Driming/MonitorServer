package com.hc.service;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.hc.dao.CollectionIndexDao;
import com.hc.dao.CollectionMonitorDao;
import com.hc.dataStorage.client.DataStorageClient;
import com.hc.entity.collection.server.CollectionServer;
import com.hc.entity.index.error.ErrorInfoRecord;
import com.hc.util.MessageUtils;
import com.hc.util.map.IndexMap;
import com.hc.util.map.ServerMap;
import com.hc.util.page.template.PageListInfo;
import com.hc.vo.IndexInfoVo;

@Service
public class CollectionIndexService {
	private IndexInfoVo indexInfoVo = null;
	
	@Autowired
	private CollectionIndexDao collectionIndexDao;
	
	@Autowired
	private WebSocketSendService webSocketSendService;
	
	@Autowired
	private CollectionMonitorDao collectionMonitorDao;
	
	public IndexInfoVo findServerInfos(Integer isUsedCs) {
		if(indexInfoVo == null)
			return findIndexInfoVo(isUsedCs); 
		return indexInfoVo;
	}
	
	public IndexInfoVo findIndexInfoVo(Integer isUsedCs){
		short type = ServerMap.COLLECT_SERVER;
		List<CollectionServer> collectServers = collectionMonitorDao.findCollectionServers(isUsedCs, type);
		type = ServerMap.STORAGE_SERVER;
		List<CollectionServer> storageServers = collectionMonitorDao.findCollectionServers(isUsedCs, type);
		type = ServerMap.SERVICE_SERVER;
		List<CollectionServer> serviceServers = collectionMonitorDao.findCollectionServers(isUsedCs, type);

		if(indexInfoVo == null)
			indexInfoVo = new IndexInfoVo();
		List<ErrorInfoRecord> errorInfos = new ArrayList<ErrorInfoRecord>();
		indexInfoVo.setCollectServers(checkSocketServers(collectServers, errorInfos));
		indexInfoVo.setStorageServers(checkRpcServers(storageServers, errorInfos));
		indexInfoVo.setServiceServers(checkSocketServers(serviceServers, errorInfos));
		
		if(!errorInfos.isEmpty()){
			webSocketSendService.indexErrorInfo(errorInfos);
			collectionIndexDao.addErrorInfos(errorInfos);
		}
		return indexInfoVo;
	}
	
	//分页获取错误信息记录
	public Object findErrorInfosByPage(int currentPage, int pageSize) {
		Page<ErrorInfoRecord> chs = collectionIndexDao.findErrorInfosByPage(currentPage, pageSize);
		PageListInfo<ErrorInfoRecord> pageInfo = new PageListInfo<ErrorInfoRecord>();
		pageInfo.setCurrentPage(currentPage);
		pageInfo.setPerPageCount(pageSize);
		pageInfo.setTotalCount((int) chs.getTotal());
		pageInfo.setPageCount(chs.getPages());
		pageInfo.setObjs(chs);

		if (currentPage <= 0 || (currentPage > chs.getPages() && currentPage != 1))
			return MessageUtils.requestPageError();
		return MessageUtils.returnSuccess(pageInfo);
	}

	private List<IndexInfoVo.ServerInfo> checkSocketServers(List<CollectionServer> collectionServers, List<ErrorInfoRecord> errorInfos) {
		List<IndexInfoVo.ServerInfo> ServerInfos = new ArrayList<IndexInfoVo.ServerInfo>();
		for (CollectionServer collectionServer : collectionServers) {
			IndexInfoVo.ServerInfo serverInfo = new IndexInfoVo.ServerInfo();
			String server = collectionServer.getServer();
			String name = collectionServer.getName();
			String[] serverInfos = server.split(":");
			serverInfo.setName(name);
			serverInfo.setServer(server);
			if(isSocketConnectable(serverInfos[0], Integer.parseInt(serverInfos[1]))){
				serverInfo.setConnect(true);
			}else{
				serverInfo.setConnect(false);
				ErrorInfoRecord errorInfo = new ErrorInfoRecord();
				errorInfo.setServer(server);
				errorInfo.setType(IndexMap.TYPE_SERVER);
				errorInfo.setName(name);
				errorInfo.setStatus("失败");
				errorInfo.setTime(System.currentTimeMillis());
				errorInfo.setReason("服务器连接失败");
				errorInfos.add(errorInfo);
			}
			ServerInfos.add(serverInfo);
		}
		return ServerInfos;
	}

	private List<IndexInfoVo.ServerInfo> checkRpcServers(List<CollectionServer> storageServers, List<ErrorInfoRecord> errorInfos) {
		List<IndexInfoVo.ServerInfo> storageServerInfos = new ArrayList<IndexInfoVo.ServerInfo>();	
		for (CollectionServer storageServer : storageServers) {
			IndexInfoVo.ServerInfo serverInfo = new IndexInfoVo.ServerInfo();
			String server = storageServer.getServer();
			String name = storageServer.getName();
			String[] serverInfos = server.split(":");
			serverInfo.setName(name);
			serverInfo.setServer(server);
			if(isRpcConnectable(serverInfos[0], Integer.parseInt(serverInfos[1]))){
				serverInfo.setConnect(true);
			}else{
				serverInfo.setConnect(false);
				ErrorInfoRecord errorInfo = new ErrorInfoRecord();
				errorInfo.setType(IndexMap.TYPE_SERVER);
				errorInfo.setServer(server);
				errorInfo.setName(name);
				errorInfo.setStatus("失败");
				errorInfo.setTime(System.currentTimeMillis());
				errorInfo.setReason("服务器连接失败");
				errorInfos.add(errorInfo);
			}
			storageServerInfos.add(serverInfo);
		}
		return storageServerInfos;
	}

	private boolean isSocketConnectable(String host, int port) {
		try {
			Socket client = new Socket();
			SocketAddress addr = new InetSocketAddress(host, port);
			// 超时500毫秒，并连接服务器
			client.connect(addr, 500);
			client.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private boolean isRpcConnectable(String host, int port) {
		try {
			DataStorageClient client = new DataStorageClient(host, port);
			client.getAllFieldCountStats();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
