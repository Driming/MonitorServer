package com.hc.util.context;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.springframework.context.ApplicationContext;

public class ContextCore {

	private static ApplicationContext context;

	public static ApplicationContext getContext() {
		return context;
	}

	public static void setContext(ApplicationContext ctx) {
		context = ctx;
	}

	public static Object getBean(Class<?> clazz) {
	     Map<String, ?> beansOfType = context.getBeansOfType(clazz);  
         Collection<?> collection = beansOfType.values();
         Iterator<?> iterator = collection.iterator();
         if(iterator.hasNext())
        	 return iterator.next();
         return null;
	}

}
