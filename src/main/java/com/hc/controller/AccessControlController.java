package com.hc.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hc.dao.AccessControlDao;
import com.hc.entity.control.access.AccessControl;
import com.hc.entity.control.middle.ra.RoleAccessMiddleKey;
import com.hc.entity.control.middle.rus.RoleUserServerMiddleKey;
import com.hc.entity.control.role.Role;
import com.hc.entity.control.user.User;
import com.hc.service.AccessControlService;
import com.hc.util.MessageUtils;
import com.hc.util.map.TaskCommandMap;
import com.hc.vo.AddUserVo;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/access/control")
public class AccessControlController {
	@Autowired
	private AccessControlService accessControlService;
	
	@Autowired
	private AccessControlDao accessControlDao;
	
	/*******权限操作*******/
	@ResponseBody
	@RequestMapping("/access/all")
	public Object getAllAccessControl(){
		return MessageUtils.returnSuccess(accessControlDao.findAllAccessControl());
	}
	
	@ResponseBody
	@RequestMapping("/access/delete")
	public Object deleteAccessControl(int acid){
		return accessControlService.deleteAccessControl(acid);
	}
	
	@ResponseBody
	@RequestMapping("/access/update")
	public Object updateAccessControl(AccessControl ac){
		return accessControlService.updateAccessControl(ac);
	}
	
	@ResponseBody
	@RequestMapping("/access/add")
	public Object addAccessControl(AccessControl ac){
		return accessControlService.addAccessControl(ac);
	}
	
	/*******角色操作*******/
	@ResponseBody
	@RequestMapping("/role/all")
	public Object getAllRoles(){
		return MessageUtils.returnSuccess(accessControlDao.findAllRoles());
	}
	
	@ResponseBody
	@RequestMapping("/role/delete")
	public Object deleteRole(int rid){
		return accessControlService.deleteRole(rid);
	}
	
	@ResponseBody
	@RequestMapping("/role/update")
	public Object updateRole(Role role){
		return accessControlService.updateRole(role);
	}
	
	@ResponseBody
	@RequestMapping("/role/add")
	public Object addRole(Role role){
		return accessControlService.addRole(role);
	}
	
	/*******用户操作*******/
	@ResponseBody
	@RequestMapping("/user/all")
	public Object findAllUsers(Short isPass){
		return MessageUtils.returnSuccess(accessControlDao.findAllUser(isPass));
	}
	
	@ResponseBody
	@RequestMapping("/user/delete")
	public Object deleteUser(int uid){
		return accessControlService.deleteUser(uid);
	}
	
	@ResponseBody
	@RequestMapping("/user/update")
	public Object updateUser(User user){
		return accessControlService.updateUser(user);
	}
	
	@ResponseBody
	@RequestMapping("/user/add")
	public Object addUser(User user){
		user.setTime(System.currentTimeMillis());
		return accessControlService.addUser(user);
	}
	
	@ResponseBody
	@RequestMapping("/user/admin/add")
	public Object addUser(@RequestBody AddUserVo auv){
		auv.getUser().setTime(System.currentTimeMillis());
		return accessControlService.addUser(auv);
	}
	
	@ResponseBody
	@RequestMapping("/user/admin/update")
	public Object updateUser(@RequestBody AddUserVo auv){
		return accessControlService.updateUser(auv);
	}
	
	@ResponseBody
	@RequestMapping("/user/reset")
	public Object resetUser(User user){
		return accessControlService.resetUser(user);
	}
	
	@ResponseBody
	@RequestMapping("/user/login")
	public Object login(User user, HttpSession session){
		return accessControlService.loginUser(user, session);
	}
	
	@ResponseBody
	@RequestMapping("/user/logout")
	public Object logout(HttpSession session){
		session.removeAttribute("webUser");
		return MessageUtils.logoutSuccess();
	}
	
	@ResponseBody
	@RequestMapping("/user/phone/send")
	public Object sendMessageByPhone(String phone){
		return accessControlService.sendMessageByPhone(phone);
	}
	
	@ResponseBody
	@RequestMapping("/user/captcha/create")
	public Object createCaptchaImg(){
		return accessControlService.createCaptchaImg();
	}
	
	@RequestMapping("/user/captcha/{img:.+}")
	public void findCaptcha(@PathVariable String img, HttpServletResponse response){
		try {
			Object obj = accessControlService.findCaptchaImg(img);
			if (obj == null)
				return;
			OutputStream outStream = response.getOutputStream();
			
			if (obj instanceof Map) {
				String error = String.valueOf(obj);
				outStream.write(JSONObject.fromObject(error).toString().getBytes());
			} else if (obj instanceof BufferedImage)
				ImageIO.write((BufferedImage) obj, "jpg", outStream);
			outStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*******角色权限关联操作*******/
	@ResponseBody
	@RequestMapping("/middle/ra/all")
	public Object getAllRoleAccesses(){
		return MessageUtils.returnSuccess(accessControlDao.findRoleAccesses());
	}
	
	@ResponseBody
	@RequestMapping("/middle/ra/update")
	public Object updateRoleAccess(RoleAccessMiddleKey ra){
		List<RoleAccessMiddleKey> ras = new LinkedList<RoleAccessMiddleKey>();
		ras.add(ra);
		return accessControlService.updateRoleAccesses(ras);
	}
	
	@ResponseBody
	@RequestMapping("/middle/ra/update/batch")
	public Object updateRoleAccesses(@RequestBody List<RoleAccessMiddleKey> ras){
		return accessControlService.updateRoleAccesses(ras);
	}
	
	
	/*******服务器用户角色关联操作*******/
	@ResponseBody
	@RequestMapping("/middle/rus/all")
	public Object getAllRoleUserServers(String csid, Integer uid){
		return MessageUtils.returnSuccess(accessControlDao.findRoleUserServers(csid, uid));
	}
	
	@ResponseBody
	@RequestMapping("/middle/rus/delete")
	public Object deleteRoleUserServer(RoleUserServerMiddleKey rus){
		return accessControlService.deleteRoleUserServer(rus);
	}
	
	@ResponseBody
	@RequestMapping("/middle/rus/add")
	public Object addRoleUserServer(RoleUserServerMiddleKey rus){
		return accessControlService.addRoleUserServer(rus);
	}
	
	@ResponseBody
	@RequestMapping(value="/middle/rus/update", method=RequestMethod.GET)
	public Object updateRoleUserServer(@ModelAttribute("oldRus") RoleUserServerMiddleKey oldRus,
			@ModelAttribute("newRus") RoleUserServerMiddleKey newRus){
		return accessControlService.updateRoleUserServer(oldRus, newRus);
	}
	
	@InitBinder("oldRus")
    public void oldRusBinder(WebDataBinder webDataBinder){
        webDataBinder.setFieldDefaultPrefix("oldRus.");
    }

    @InitBinder("newRus")
    public void newRusBinder(WebDataBinder webDataBinder){
        webDataBinder.setFieldDefaultPrefix("newRus.");
    }
	
	@ResponseBody
	@RequestMapping("/test")
	public Object getTest(HttpSession session){
		return TaskCommandMap.commandMap;
	}

}
