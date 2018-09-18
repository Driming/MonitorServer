/*package com.hc.service;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSession.Receiptable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.web.socket.sockjs.frame.Jackson2SockJsMessageCodec;

import com.hc.dao.CollectionMonitorDao;
import com.hc.entity.collection.history.CollectionHistory;
import com.hc.entity.collection.history.CollectionHistory.Error;
import com.hc.entity.collection.server.CollectionServer;
import com.hc.entity.collection.task.CollectionTask;
import com.hc.entity.config.RequestConfigCache;
import com.hc.interceptor.WebSocketHandler;
import com.hc.util.StringUtils;
import com.hc.vo.ProcessVo;
import com.hc.vo.RecordVo;
import com.hc.vo.RecordVo.Statistic;
import com.hc.vo.RecordVo.Statistic.Outcome;
import com.hc.vo.RecordVo.Statistic.Source;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@Service
public class StompClientService {
	private static Logger logger = Logger.getLogger(StompClientService.class);
	private final static WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
	private Map<String, RequestConfigCache> configCaches= new ConcurrentHashMap<String, RequestConfigCache>();
	private Map<String, StompSession> stompSessions = new ConcurrentHashMap<String, StompSession>();
	
	@Autowired
	private CollectionMonitorDao collectionStatusDao;
	
    private SimpMessagingTemplate serverTemplate;  
    
	public void setServerTemplate(SimpMessagingTemplate serverTemplate) {
		this.serverTemplate = serverTemplate;
	}

	@PostConstruct
	public void initConnect(){
		List<RequestConfigCache> caches = collectionStatusDao.findConfigCache();
		for(RequestConfigCache cache : caches)
			configCaches.put(cache.getCsid(), cache);
		
		//连接服务器stomp
		for(Entry<String, RequestConfigCache> entry : configCaches.entrySet()){
			RequestConfigCache config = entry.getValue();
			connectingAndSubscribe(config, null);
		}
	}
	
	//新服务器连接stomp,并订阅
	public void newConnect(RequestConfigCache config){
		//增加新服务器stomp信息
		configCaches.put(config.getCsid(), config);
		connectingAndSubscribe(config, null);
	}
	
    //stomp连接并订阅
    public void connectingAndSubscribe(RequestConfigCache config, String csid){
    	try {
			ListenableFuture<StompSession> lf = connect(
					config.getIp(), config.getPort(), config.getRoot(), csid);
			StompSession stompSession = lf.get();
			
			//保存stompSession
			stompSessions.put(config.getCsid(), stompSession);
			stompSession.setAutoReceipt(true);  
			
			//订阅消息
			subscribeProcess(config.getProcess(), stompSession);
			subscribeHeartbeat(config.getHeartbeat(), stompSession);
			subscribeRecord(config.getRecord(), stompSession);
			subscribeStatus(config.getStatus(), stompSession);
			subscribeFeedback(config.getFeedback(), stompSession);
			subscribeHistory("/user/test1/synchronize/history", stompSession);
			logger.info("stomp connect success:" + config.getIp());
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("stomp connect fail:" + config.getIp());
		}
    }
    
	public ListenableFuture<StompSession> connect(String ip, String port, String root, String csid) {
        Transport webSocketTransport = new WebSocketTransport(new StandardWebSocketClient());
        List<Transport> transports = Collections.singletonList(webSocketTransport);

        SockJsClient sockJsClient = new SockJsClient(transports);
        sockJsClient.setMessageCodec(new Jackson2SockJsMessageCodec());
        
        WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
        
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.afterPropertiesSet();
        stompClient.setTaskScheduler(taskScheduler); // for heartbeats

        String url = StringUtils.join("ws://", ip, ":", port, root, "?csid=", csid);
        return stompClient.connect(url, headers, new WebSocketHandler());
    }

    public void subscribeProcess(String subsribeName, StompSession stompSession) throws ExecutionException, InterruptedException {
        stompSession.subscribe(subsribeName, new StompFrameHandler() {

            public Type getPayloadType(StompHeaders stompHeaders) {
                return byte[].class;
            }

            public void handleFrame(StompHeaders stompHeaders, Object obj) {
               try{
	               String message = new String((byte[]) obj);
	               System.out.println(message);
	               @SuppressWarnings("unchecked")
	               List<ProcessVo> pvs = JSONArray.toList(JSONArray.fromObject(message), new ProcessVo(), new JsonConfig());
	               List<CollectionTask> cts = new LinkedList<CollectionTask>();
	               for(ProcessVo pv : pvs){
	            	   CollectionTask ct = new CollectionTask();
	            	   ct.setCtid(pv.getId());
	            	   ct.setPid(pv.getPid());
	            	   ct.setUsedCpu(pv.getCpu());
	            	   ct.setUsedMemory((float)pv.getMemory());
	            	   if(pv.getNetwork() != null){
	            		   ct.setUpload((float)pv.getNetwork().getUp());
	            		   ct.setDownload((float)pv.getNetwork().getDown());
	            	   }
	            	   if(pv.getIo() != null){
	            		   ct.setReadSpeed((float)pv.getIo().getRead());
	            		   ct.setWriteSpeed((float)pv.getIo().getWrite());
	            	   }	
	            	   cts.add(ct);
	               }
	               //存入数据库
	               collectionStatusDao.updateCollectionTaskBatch(cts);
	               //发送给前端
	               serverTemplate.convertAndSend("/client/process", JSONArray.fromObject(cts, StringUtils.jsonIgnoreNull()));
               }catch(Exception e){
            	   e.printStackTrace();
               }
            }
        });
    }
    
    public void subscribeRecord(String subsribeName, StompSession stompSession) throws ExecutionException, InterruptedException {
        stompSession.subscribe(subsribeName, new StompFrameHandler() {

            public Type getPayloadType(StompHeaders stompHeaders) {
                return byte[].class;
            }

            public void handleFrame(StompHeaders stompHeaders, Object obj) {
                try{
                	String message = new String((byte[]) obj);
                	Map<String, Object> classMap = new HashMap<String, Object>();
                	classMap.put("exception", com.hc.vo.RecordVo.Exception.class);
  	                RecordVo rv = (RecordVo) JSONObject.toBean(JSONObject.fromObject(message), RecordVo.class, classMap);
                	CollectionHistory ch = new CollectionHistory();
                	CollectionServer cs = collectionStatusDao.findCollectionServerByCtid(rv.getId(), null);
                	if(cs != null){
                		ch.setCsid(cs.getCsid());
                		ch.setCtid(cs.getCts().get(0).getCtid());
                	}
                	ch.setStatus(rv.getStatus());
                	ch.setTime(rv.getTime());
                	ch.setDelay(rv.getDelay());
                	ch.setRetry(rv.getRetry());
                	Statistic statistic = rv.getStatistic();
                	if(statistic != null){
                		Source source = statistic.getSource();
                		if(source != null){
                			ch.setDataSourceNum(source.getSum());
                			ch.setDataSourceSize(source.getSize());
                		}
                		Outcome outcome = statistic.getOutcome();
                		if(outcome != null){
                			ch.setDataResultNum(outcome.getSum());
                			ch.setDataResultSize(outcome.getSize());
                		}
                	}
                	List<Error> errors = new LinkedList<Error>();
                    List<com.hc.vo.RecordVo.Exception> es = rv.getException();
                    if(es != null){
	                    for(com.hc.vo.RecordVo.Exception e : es){
	                    	Error error = new CollectionHistory().new Error();
	                    	error.setFileName(e.getFilename());
	                    	error.setMessage(e.getMessage());
	                    	errors.add(error);
	                    }
                    }
                    ch.setErrors(errors);
                    
                    //插入数据库
                    collectionStatusDao.addCollectionHistory(ch);
                    //更新Task数据表
                    collectionStatusDao.updateCollectionTaskByHistory(ch);
                    //向前端发送更改
                    CollectionTask ct = collectionStatusDao.findCollectionTaskByHistoryUpdate(rv.getId(), null);
                    JSONObject resultJson  = JSONObject.fromObject(ct, StringUtils.jsonIgnoreNull());
                    serverTemplate.convertAndSend("/client/record", resultJson);
                    //向前端发送含有错误信息的任务
                    if(!ch.getErrors().isEmpty()){
                    	JSONObject errorJson = JSONObject.fromObject(ch, StringUtils.jsonIgnoreNull());
                        serverTemplate.convertAndSend("/client/error", errorJson);
                    }
                }catch(Exception e){
                	e.printStackTrace();
                }
            }
        });
    }
    
    public void subscribeStatus(String subsribeName, StompSession stompSession) throws ExecutionException, InterruptedException {
        stompSession.subscribe(subsribeName, new StompFrameHandler() {

            public Type getPayloadType(StompHeaders stompHeaders) {
                return byte[].class;
            }

            public void handleFrame(StompHeaders stompHeaders, Object obj) {
            	try{
            		String message = new String((byte[]) obj);
                    JSONObject processJson = JSONObject.fromObject(message);
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("ctid", processJson.get("id"));
                    map.put("status", processJson.get("status"));
                    processJson.clear();
                    processJson = JSONObject.fromObject(map);
                    //存入数据库
                    CollectionTask ct = new CollectionTask();
                    ct.setCtid(processJson.getString("ctid"));
                    ct.setStatus(processJson.getString("status"));
                    collectionStatusDao.updateCollectionTaskSelective(ct);
                    //发送给前端
                    serverTemplate.convertAndSend("/client/status", processJson);
            	}catch(Exception e){
            		e.printStackTrace();
            	}
            }
        });
    }
    
    public void subscribeHeartbeat(String subsribeName, StompSession stompSession) throws ExecutionException, InterruptedException {
       stompSession.subscribe(subsribeName, new StompFrameHandler() {

            public Type getPayloadType(StompHeaders stompHeaders) {
                return byte[].class;
            }

            public void handleFrame(StompHeaders stompHeaders, Object obj) {
            	//心跳
            	System.out.println(new String((byte[])obj));
            }
        });
    }
    
    public void subscribeFeedback(String subsribeName, StompSession stompSession) throws ExecutionException, InterruptedException {
        stompSession.subscribe(subsribeName, new StompFrameHandler() {

             public Type getPayloadType(StompHeaders stompHeaders) {
                 return byte[].class;
             }

             public void handleFrame(StompHeaders stompHeaders, Object obj) {
             	//反馈
             	System.out.println(new String((byte[])obj));
             }
         });
     }
    
    private void subscribeHistory(String subsribeName, StompSession stompSession) {
    	stompSession.subscribe(subsribeName, new StompFrameHandler() {

            public Type getPayloadType(StompHeaders stompHeaders) {
                return byte[].class;
            }

            public void handleFrame(StompHeaders stompHeaders, Object obj) {
            	//反馈
            	//System.out.println(new String((byte[])obj));
            }
        });		
	}

    public void sendMessage(String csid, String subsribeName,final String message) {
    	StompSession stompSession = stompSessions.get(csid);
    	Receiptable receipt = null;
    	if(stompSession != null){
    		receipt = stompSession.send(subsribeName, message.getBytes());
	    	receipt.addReceiptTask(new Runnable() {  
	             @Override  
	             public void run() {  
	            	 System.out.println(message + "----发送成功");
	             }   
	         });  
    	}
    }
    
    public void sendMessage(String csid, String subsribeName,final byte[] bytes) {
    	StompSession stompSession = stompSessions.get(csid);
    	Receiptable receipt = null;
    	if(stompSession != null){
    		StompHeaders stompHeaders = new StompHeaders();
    		stompHeaders.setContentType(MimeTypeUtils.APPLICATION_OCTET_STREAM);
    		stompHeaders.setDestination(subsribeName);
    		receipt = stompSession.send(stompHeaders, bytes);
    		//receipt = stompSession.send(subsribeName, bytes);
	    	receipt.addReceiptTask(new Runnable() {  
	             @Override  
	             public void run() {  
	             }   
	         });  
    	}
    }
    
	public Map<String, RequestConfigCache> getConfigCaches() {
		return configCaches;
	}

	public Map<String, StompSession> getStompSessions() {
		return stompSessions;
	}
	
}


*/