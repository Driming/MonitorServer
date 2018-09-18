package com.hc.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hc.dao.AccessControlDao;
import com.hc.entity.control.access.AccessControl;
import com.hc.entity.control.middle.ra.RoleAccessMiddleKey;
import com.hc.entity.control.middle.rus.RoleUserServerMiddleKey;
import com.hc.entity.control.role.Role;
import com.hc.entity.control.user.User;
import com.hc.entity.sms.SmsStatus;
import com.hc.util.MessageUtils;
import com.hc.util.map.cache.VerificationCaches;
import com.hc.util.message.CreateCaptcha;
import com.hc.util.message.PhoneSms;
import com.hc.util.verification.MessageVerifier;
import com.hc.vo.AddUserVo;

import net.sf.json.JSONObject;

@Service
public class AccessControlService {
	@Autowired
	private AccessControlDao accessControlDao;

	/*********权限操作**********/
	public Object deleteAccessControl(int acid) {
		int result = accessControlDao.deleteAccessControl(acid);
		if(result <= 0)
			return MessageUtils.operationFailedError();
		return MessageUtils.operationSuccess();
	}

	public Object updateAccessControl(AccessControl ac) {
		if(ac == null 
				|| ac.getAcid() == null 
				|| ac.getName() == null)
			return MessageUtils.parameterNullError();
		
		int result = accessControlDao.updateAccessControl(ac);
		if(result <= 0)
			return MessageUtils.operationFailedError();
		return MessageUtils.operationSuccess();
	}

	public Object addAccessControl(AccessControl ac) {
		if(ac == null || ac.getName() == null)
			return MessageUtils.parameterNullError();
		
		Integer maxAcid = accessControlDao.findMaxAcid();
		ac.setAcid(maxAcid == null ? 1 : maxAcid*2);
		
		int result = accessControlDao.addAccessControl(ac);
		if(result <= 0)
			return MessageUtils.operationFailedError();
		return MessageUtils.operationSuccess();
	}

	
	/*********角色操作**********/
	public Object deleteRole(int rid) {
		int result = accessControlDao.deleteRole(rid);
		if(result <= 0)
			return MessageUtils.operationFailedError();
		return MessageUtils.operationSuccess();
	}

	public Object updateRole(Role role) {
		if(role == null 
				|| role.getRid() == null 
				|| role.getName() == null)
			return MessageUtils.parameterNullError();
		
		int result = accessControlDao.updateRole(role);
		if(result <= 0)
			return MessageUtils.operationFailedError();
		return MessageUtils.operationSuccess();
	}

	public Object addRole(Role role) {
		if(role == null || role.getName() == null)
			return MessageUtils.parameterNullError();
		
		int result = accessControlDao.addRole(role);
		if(result <= 0)
			return MessageUtils.operationFailedError();
		return MessageUtils.operationSuccess();
	}

	
	/*********用户操作**********/
	//手机发短信验证码
	public Object sendMessageByPhone(String phone) {
		if(phone == null)
			return MessageUtils.parameterNullError();
		
		//防止频繁发短信
		boolean isTooFrequent = MessageVerifier.isTooFrequent(phone);
		if(isTooFrequent)
			return MessageUtils.phoneMessageTooFrequentError();
		
		String code = PhoneSms.getCode();
		String text = "您的验证码是"+code+"。如非本人操作，请忽略本短信";
		try {
			String sendSms = PhoneSms.sendSms(text, phone);
			JSONObject smsJson = JSONObject.fromObject(sendSms);
			if(smsJson.containsKey("code") && smsJson.getInt("code") == 0){
				SmsStatus smsStatus = new SmsStatus(code, System.currentTimeMillis());
				VerificationCaches.put(phone, smsStatus);
				return MessageUtils.operationSuccess();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return MessageUtils.operationFailedError();
	}
	
	public Object deleteUser(int uid) {
		int result = accessControlDao.deleteUser(uid);
		if(result <= 0)
			return MessageUtils.operationFailedError();
		return MessageUtils.operationSuccess();
	}

	//用户更新
	public synchronized Object updateUser(User user) {
		if(user == null || user.getUid() == null)
			return MessageUtils.parameterNullError();
		
		int result = -1;
		if(user.getIsPass() != null && user.getIsPass() == 0)
			result = accessControlDao.deleteUser(user.getUid());
		else{
			if(user.getUsername() != null || user.getPhone() != null){
				List<User> existUser = accessControlDao.findUserByUsername(
						user.getUid(), user.getUsername(), user.getPhone());
				if(!existUser.isEmpty())
					return MessageUtils.dataHasExistError();
			}
			
		 	result = accessControlDao.updateUser(user);
		}
		if(result <= 0)
			return MessageUtils.operationFailedError();
		return MessageUtils.operationSuccess();
	}
	
	//管理员添加用户
	public synchronized Object addUser(AddUserVo auv) {
		User user = auv.getUser();
		if(user == null
				|| user.getUsername() == null 
				|| user.getPassword() == null 
				|| user.getPhone() == null)
			return MessageUtils.parameterNullError();
		
		List<User> existUser = accessControlDao.findUserByUsername(null, 
				user.getUsername(), user.getPhone());
		if(!existUser.isEmpty())
			return MessageUtils.dataHasExistError();
		
		accessControlDao.addUser(user);
		if(user.getUid() == null)
			return MessageUtils.operationFailedError();
		List<RoleUserServerMiddleKey> ruses = auv.getRuses();
		for(RoleUserServerMiddleKey rus : ruses)
			rus.setUid(user.getUid());
		
		accessControlDao.addRoleUserServerBatch(ruses);
		return MessageUtils.operationSuccess();
	}
	
	//管理员更新用户
	public synchronized Object updateUser(AddUserVo auv) {
		User user = auv.getUser();
		if(user == null
				|| user.getUid() == null
				|| user.getUsername() == null 
				|| user.getPassword() == null 
				|| user.getPhone() == null)
			return MessageUtils.parameterNullError();
		
		List<User> existUser = accessControlDao.findUserByUsername(
				user.getUid(), user.getUsername(), user.getPhone());
		if(!existUser.isEmpty())
			return MessageUtils.dataHasExistError();
		
		List<RoleUserServerMiddleKey> ruses = auv.getRuses();
		for(RoleUserServerMiddleKey rus : ruses)
			rus.setUid(user.getUid());
		
		accessControlDao.updateUser(user);
		accessControlDao.deleteRoleUserServerByUid(user.getUid());
		accessControlDao.addRoleUserServerBatch(ruses);
		return MessageUtils.operationSuccess();
	}


	//注册用户
	public synchronized Object addUser(User user) {
		if(user == null 
				|| user.getUsername() == null
				|| user.getPassword() == null 
				|| user.getPhone() == null
				|| user.getVerificationCode() == null
				|| user.getCaptchaFlag() == null)
			return MessageUtils.parameterNullError();
		
		//验证验证码
		boolean isCaptchaTure = MessageVerifier.validateCaptcha(user);
		if(!isCaptchaTure)
			return MessageUtils.capatchaCodeFailureError();
		
		//验证短信
		boolean isPhoneVerificationTrue = MessageVerifier.validatePhone(user);
		if(!isPhoneVerificationTrue)
			return MessageUtils.verificationCodeFailureError();
		
		List<User> existUser = accessControlDao.findUserByUsername(null, 
				user.getUsername(), user.getPhone());
		if(!existUser.isEmpty())
			return MessageUtils.dataHasExistError();
		
		int result = accessControlDao.addUser(user);
		if(result <= 0)
			return MessageUtils.operationFailedError();
		return MessageUtils.operationSuccess();
	}
	
	//重置密码
	public Object resetUser(User user) {
		if(user.getUid() == null
				|| user.getPassword() == null
				|| user.getPhone() == null 
				|| user.getVerificationCode() == null
				|| user.getCaptcha() == null
				|| user.getCaptchaFlag() == null)
			return MessageUtils.parameterNullError();
		
		//验证身份
		User existUser = accessControlDao.findUserByUidAndPhone(user.getUid(), user.getPhone());
		if(existUser == null)
			return MessageUtils.permissionDeniedError();
		
		//验证验证码
		boolean isCapatchaTure = MessageVerifier.validateCaptcha(user);
		if(!isCapatchaTure)
			return MessageUtils.capatchaCodeFailureError();
		
		//验证短信
		boolean isPhoneVerificationTrue = MessageVerifier.validatePhone(user);
		if(!isPhoneVerificationTrue)
			return MessageUtils.verificationCodeFailureError();
		
		int result = accessControlDao.updateUser(user);
		if(result <= 0)
			return MessageUtils.operationFailedError();
		return MessageUtils.operationSuccess();
	}

	public Object loginUser(User user, HttpSession session) {
		if(user == null 
				|| (user.getUsername() == null 
				&& user.getPhone() == null) 
				|| user.getPassword() == null)
			return MessageUtils.parameterNullError();
		
		User loginUser = accessControlDao.loginUser(user);
		if(loginUser == null)
			return MessageUtils.loginError();
		else if(loginUser.getIsPass() == 0)
			return MessageUtils.permissionDeniedError();
		
		session.setAttribute("webUser", loginUser);
		return MessageUtils.returnSuccess(loginUser);
	}
	
	//生成验证码图片
	public synchronized Object createCaptchaImg() {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		CreateCaptcha captchaUtils = CreateCaptcha.Instance();
		long time = System.currentTimeMillis();
		SmsStatus smsStatus = new SmsStatus(captchaUtils.getString(), time);
		VerificationCaches.put(String.valueOf(time), smsStatus);
		
		returnMap.put("flag", time);
		returnMap.put("url", accessControlDao.saveCaptcha(captchaUtils.getImage(), time));
		return MessageUtils.returnSuccess(returnMap);
	}
	
	public Object findCaptchaImg(String img) {
		BufferedImage image = accessControlDao.findCaptchaImg(img);
		if(image == null)
			return MessageUtils.imgNotFountError();
		return image;
	}

	
	/*******角色权限关联操作*******/
	public Object updateRoleAccesses(List<RoleAccessMiddleKey> ras) {
		if(ras == null)
			return MessageUtils.parameterNullError();
		
		List<Integer> rids = new LinkedList<Integer>();
		List<Integer> acids = new LinkedList<Integer>();
		for(RoleAccessMiddleKey ra : ras){
			if(ra.getRid() != null)
				rids.add(ra.getRid());
			if(ra.getAcids() != null)
				acids.addAll(ra.getAcids());
		}
		
		accessControlDao.deleteRoleAccessBatch(rids);
		if(acids.size() > 0)
			accessControlDao.addRoleAccessBatch(ras);
		return MessageUtils.operationSuccess();
	}

	/*******服务器用户角色关联操作*******/
	public Object deleteRoleUserServer(RoleUserServerMiddleKey rus) {
		if(rus == null 
				||rus.getRid() == null
				|| rus.getCsid() == null 
				|| rus.getUid() == null)
			return MessageUtils.parameterNullError();
		
		int result = accessControlDao.deleteRoleUserServer(rus);
		if(result <= 0)
			return MessageUtils.operationFailedError();
		return MessageUtils.operationSuccess();
	}

	public Object addRoleUserServer(RoleUserServerMiddleKey rus) {
		if(rus == null 
				||rus.getRid() == null
				|| rus.getCsid() == null 
				|| rus.getUid() == null)
			return MessageUtils.parameterNullError();
		
		int result = accessControlDao.addRoleUserServer(rus);
		if(result <= 0)
			return MessageUtils.operationFailedError();
		return MessageUtils.operationSuccess();
	}

	public Object updateRoleUserServer(RoleUserServerMiddleKey oldRus,
			RoleUserServerMiddleKey newRus) {
		if(oldRus == null 
				|| oldRus.getRid() == null 
				|| oldRus.getCsid() == null
				|| oldRus.getUid() == null 
				|| newRus == null 
				|| newRus.getRid() == null
				|| newRus.getCsid() == null 
				|| newRus.getUid() == null)
			return MessageUtils.parameterNullError();
		
		int result = accessControlDao.updateRoleUserServer(oldRus, newRus);
		if(result <= 0)
			return MessageUtils.operationFailedError();
		return MessageUtils.operationSuccess();
	}

}
