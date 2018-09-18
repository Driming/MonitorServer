package com.hc.interceptor;

import java.security.Principal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

public class ServerHandshakeHandler extends DefaultHandshakeHandler{
	
	@Override
	protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
			Map<String, Object> attributes) {
		MyPrincipal principal = new MyPrincipal();
		if (request instanceof ServerHttpRequest) {
			ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
			HttpServletRequest hsRequest = servletRequest.getServletRequest();
			String csid = hsRequest.getParameter("csid");
			if(csid != null){
				principal.setName(csid);
				return principal;
			}
		}
		
		return request.getPrincipal();
	}
	
	class MyPrincipal implements Principal{
		private String name = "name";
		

		public void setName(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}
		
	}
}
