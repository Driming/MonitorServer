package com.hc.interceptor;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;

import com.hc.util.count.CountWebsocketConnected;
import com.hc.util.count.CountWebsocketConnected.DetailConnected;

public class PresenceChannelInterceptor extends ChannelInterceptorAdapter{
	private SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
  
	@Override  
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {  
    	StompHeaderAccessor sha = StompHeaderAccessor.wrap(message);  
        if(sha.getCommand() == null) {  
            return;  
        }  
        //判断客户端的连接状态  
        switch(sha.getCommand()) {  
            case CONNECT:  
                connect(sha.getUser());  
                break;  
            case CONNECTED:  
                break;  
            case DISCONNECT:  
                disconnect(sha.getUser());  
                break;  
            default:  
                break;  
        }  
    }  
  
    //连接成功  
    private void connect(Principal principal){  
    	synchronized (format) {
    		DetailConnected detailConnected = new DetailConnected();
    		CountWebsocketConnected.connectedNum++;
    		detailConnected.setCsid(principal.getName());
    		detailConnected.setIndex(CountWebsocketConnected.connectedNum);
    		detailConnected.setTime(format.format(new Date()));
    		detailConnected.setStatus("connect");
    		CountWebsocketConnected.details.add(detailConnected);
		}
    
    }  
  
    //断开连接  
    private void disconnect(Principal principal){  
    	synchronized (format) {
    		DetailConnected detailConnected = new DetailConnected();
    		CountWebsocketConnected.connectedNum--;
    		detailConnected.setCsid(principal.getName());
    		detailConnected.setIndex(CountWebsocketConnected.connectedNum);
    		detailConnected.setTime(format.format(new Date()));
    		detailConnected.setStatus("disconnect");
    		CountWebsocketConnected.details.add(detailConnected);
		}
    	
    }  
}
