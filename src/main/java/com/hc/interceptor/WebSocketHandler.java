package com.hc.interceptor;

import org.apache.log4j.Logger;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

@Deprecated
public class WebSocketHandler extends StompSessionHandlerAdapter {
	  private static Logger logger = Logger.getLogger(WebSocketHandler.class);

	  public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
	      logger.info("Now connected");
	  }
}