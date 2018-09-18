package com.hc.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

import com.hc.interceptor.PresenceChannelInterceptor;
import com.hc.interceptor.ServerHandshakeHandler;
import com.hc.interceptor.ServerHandshakeInterceptor;

@Configuration  
@EnableWebSocketMessageBroker  
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer{
	
	@Override  
    public void configureMessageBroker(MessageBrokerRegistry config) {  
        config.enableSimpleBroker("/user","/client");
        config.setApplicationDestinationPrefixes("/server");  
        config.setUserDestinationPrefix("/user/");  
    }
  
    @Override  
    public void registerStompEndpoints(StompEndpointRegistry registry) {  
        registry.addEndpoint("/ws")
        .setAllowedOrigins("*")
        .setHandshakeHandler(serverHandshakeHandler())
        .addInterceptors(serverHandshakeInterceptor()).withSockJS();
    }
    
	@Override
	public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
		registration.setMessageSizeLimit(128*1024*1024)
				.setSendBufferSizeLimit(128*1024*1024)
				.setSendTimeLimit(5*60*1000);
		super.configureWebSocketTransport(registration);
	}
	  
    @Override  
    public void configureClientInboundChannel(ChannelRegistration registration) {  
        registration.setInterceptors(presenceChannelInterceptor());  
    }  
   
    @Override  
    public void configureClientOutboundChannel(ChannelRegistration registration) {  
        registration.setInterceptors(presenceChannelInterceptor());  
    } 
    
    @Bean  
    public ServerHandshakeInterceptor serverHandshakeInterceptor() {  
        return new ServerHandshakeInterceptor();  
    } 
    
    
    @Bean  
    public ServerHandshakeHandler serverHandshakeHandler() {  
        return new ServerHandshakeHandler();  
    } 
    
	@Bean  
    public PresenceChannelInterceptor presenceChannelInterceptor() {  
        return new PresenceChannelInterceptor();  
    }  
    
}
