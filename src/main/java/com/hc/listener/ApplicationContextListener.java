package com.hc.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.hc.util.context.ContextCore;

public class ApplicationContextListener implements ServletContextListener{
	public void contextInitialized(ServletContextEvent event) {  
	       ServletContext context = event.getServletContext();  
	       try {  
	           initContextUtil(context);  
	            
	       } catch (Exception ex) {  
	           ex.printStackTrace();  
	       }  
	    }  
	   
	    public void contextDestroyed(ServletContextEvent sce) {  
	   
	    }  
	   
	        
	    private void initContextUtil(ServletContext context) throws Exception{  
	           ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(context);  
	           ContextCore.setContext(ctx);  
	            
	    }  
}
