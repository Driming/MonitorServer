package com.hc.util.map.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class VerificationCaches {
	private static Map<String, Object> caches = new ConcurrentHashMap<String, Object>();

	public synchronized static Object put(String key, Object value) {
		return caches.put(key, value);
	}

	public synchronized static Object delete(String key) {
		return caches.remove(key);
	}
	
	public static Object get(String key){
		return caches.get(key);
	}

	public static Map<String, Object> getCaches() {
		return caches;
	}

}
