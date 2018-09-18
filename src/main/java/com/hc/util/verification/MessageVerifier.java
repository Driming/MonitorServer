package com.hc.util.verification;

import com.hc.entity.control.user.User;
import com.hc.entity.sms.SmsStatus;
import com.hc.util.map.cache.VerificationCaches;

public class MessageVerifier {
	
	private static final long PHONE_INTERVAL_MESSAGE_TIME = 30*1000;
	private static final long PHONE_EFFECTIVE_TIME = 30*60*1000;
	private static final long CAPTCHA_EFFECTIVE_TIME = 5*60*1000;
	
	public static boolean isTooFrequent(String phone){
		SmsStatus existSms = (SmsStatus) VerificationCaches.get(phone);
		if(existSms != null 
				&& System.currentTimeMillis() - existSms.getTime() < PHONE_INTERVAL_MESSAGE_TIME)
			return true;
		return false;
	}
	
	public static boolean validatePhone(User user){
		SmsStatus smsStatus = (SmsStatus) VerificationCaches.get(user.getPhone());
		if(smsStatus == null 
				|| !smsStatus.getCode().equals(user.getVerificationCode()) 
				|| System.currentTimeMillis() - smsStatus.getTime() > PHONE_EFFECTIVE_TIME)
			return false;
		return true;
	}
	
	public static boolean validateCaptcha(User user){
		SmsStatus vcSmsStatus = (SmsStatus) VerificationCaches.get(user.getCaptchaFlag());
		if(vcSmsStatus == null 
				|| !vcSmsStatus.getCode().equals(user.getCaptcha())
				|| System.currentTimeMillis() - vcSmsStatus.getTime() > CAPTCHA_EFFECTIVE_TIME)
			return false;
		return true;
	}
}
