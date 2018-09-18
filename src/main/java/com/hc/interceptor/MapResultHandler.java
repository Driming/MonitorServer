package com.hc.interceptor;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

public class MapResultHandler implements ResultHandler{

	    private final Map<String, Object> mappedResults = new HashMap<String, Object>();  
	  
		@Override  
	    public void handleResult(ResultContext context) {  
	        Map<?, ?> map = (Map<?, ?>) context.getResultObject();   
	        mappedResults.put((String)map.get("key"), map.get("value"));  // xml 配置里面的property的值，对应的列  
	    }  
	    public Map<String, Object> getMappedResults() {    
	        return mappedResults;    
	    }    
}
