package com.hc.task;

import java.util.Map;
import java.util.Map.Entry;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hc.entity.sms.SmsStatus;
import com.hc.util.map.cache.VerificationCaches;

@Component
public class CacheManagerTask {
	private final long KEEP_LIVE = 60*60*1000;

	@Scheduled(cron = "0 * * * * ? ")
	public void cleanCache() {
		Map<String, Object> caches = VerificationCaches.getCaches();
		long current = System.currentTimeMillis();
		for(Entry<String, Object> entry : caches.entrySet()){
			String key = entry.getKey();
			Object value = entry.getValue();
			if (value instanceof SmsStatus) {
				SmsStatus smsStatus = (SmsStatus) value;
				long time = smsStatus.getTime();
				if(current - time > KEEP_LIVE)
					VerificationCaches.delete(key);
			}
		}
	}
}
