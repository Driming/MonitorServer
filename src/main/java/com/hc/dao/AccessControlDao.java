package com.hc.dao;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hc.entity.control.access.AccessControl;
import com.hc.entity.control.access.AccessControlMapper;
import com.hc.entity.control.middle.ra.RoleAccessMiddleKey;
import com.hc.entity.control.middle.ra.RoleAccessMiddleMapper;
import com.hc.entity.control.middle.rus.RoleUserServerMiddleKey;
import com.hc.entity.control.middle.rus.RoleUserServerMiddleMapper;
import com.hc.entity.control.role.Role;
import com.hc.entity.control.role.RoleMapper;
import com.hc.entity.control.user.User;
import com.hc.entity.control.user.UserMapper;
import com.hc.util.StringUtils;
import com.hc.util.properties.PropertiesBuilder;

@Repository
public class AccessControlDao {

	private final String captchaPath = StringUtils.addSeparator(PropertiesBuilder.buildProperties().getCaptcha());
	private final String captchaUrlPath = StringUtils.addSeparator(PropertiesBuilder.buildProperties().getCaptchaUrl());
	
	@Autowired
	private AccessControlMapper accessControlMapper;

	@Autowired
	private RoleMapper roleMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private RoleAccessMiddleMapper roleAccessMiddleMapper;

	@Autowired
	private RoleUserServerMiddleMapper roleUserServerMiddleMapper;

	/******* 权限单元 *******/
	public List<AccessControl> findAllAccessControl() {
		return accessControlMapper.selectAll();
	}

	public int addAccessControl(AccessControl ac) {
		return accessControlMapper.insertAccessControlSelective(ac);
	}

	public int deleteAccessControl(int acid) {
		return accessControlMapper.deleteAccessControl(acid);
	}

	public int updateAccessControl(AccessControl ac) {
		return accessControlMapper.updateAccessControlSelective(ac);
	}

	public Integer findMaxAcid() {
		return accessControlMapper.selectMaxAcid();
	}

	/******* 角色操作 *******/
	public List<Role> findAllRoles() {
		return roleMapper.selectAll();
	}

	public int deleteRole(int rid) {
		return roleMapper.deleteRole(rid);
	}

	public int updateRole(Role role) {
		return roleMapper.updateRoleSelective(role);
	}

	public int addRole(Role role) {
		return roleMapper.insertRoleSelective(role);
	}

	/******* 用户操作 *******/
	public List<User> findAllUser(Short isPass) {
		return userMapper.selectAll(isPass);
	}

	public int deleteUser(int uid) {
		return userMapper.deleteUser(uid);
	}

	public int updateUser(User user) {
		return userMapper.updateUsreSelective(user);
	}

	public int addUser(User user) {
		return userMapper.insertUserSelective(user);
	}

	public List<User> findUserByUsername(Integer uid, String username, String phone) {
		return userMapper.selectUserByUsername(uid, username, phone);
	}

	public User loginUser(User user) {
		return userMapper.loginUser(user);
	}

	public User findUserByUidAndPhone(Integer uid, String phone) {
		return userMapper.selectUserByUidAndPhone(uid, phone);
	}

	/******* 角色权限关联操作 *******/
	public List<RoleAccessMiddleKey> findRoleAccesses() {
		return roleAccessMiddleMapper.selectRoleAccesses();
	}

	public int deleteRoleAccessBatch(List<Integer> rids) {
		return roleAccessMiddleMapper.deleteRoleAccessBatch(rids);
	}

	public int addRoleAccessBatch(List<RoleAccessMiddleKey> ras) {
		return roleAccessMiddleMapper.insertRoleAccessBatch(ras);
	}

	/*******
	 * 服务器角色用户关联操作
	 * 
	 * @param uid
	 * @param csid
	 *******/
	public List<RoleUserServerMiddleKey> findRoleUserServers(String csid, Integer uid) {
		return roleUserServerMiddleMapper.selectAll(csid, uid);
	}

	public int deleteRoleUserServer(RoleUserServerMiddleKey rus) {
		return roleUserServerMiddleMapper.deleteRoleUserServer(rus);
	}

	public int deleteRoleUserServerByUid(Integer uid) {
		return roleUserServerMiddleMapper.deleteRoleUserServerByUid(uid);
	}

	public int updateRoleUserServer(RoleUserServerMiddleKey oldRus, RoleUserServerMiddleKey newRus) {
		return roleUserServerMiddleMapper.updateRoleUserServer(oldRus, newRus);
	}

	public int addRoleUserServer(RoleUserServerMiddleKey rus) {
		return roleUserServerMiddleMapper.insertRoleUserServer(rus);
	}

	public int addRoleUserServerBatch(List<RoleUserServerMiddleKey> ruses) {
		return roleUserServerMiddleMapper.insertRoleUserServerBatch(ruses);
	}

	public List<Integer> findUserAccesses(String csid, Integer uid) {
		return roleUserServerMiddleMapper.selectUserAccesses(csid, uid);
	}

	public BufferedImage findCaptchaImg(String img) {
		String path = StringUtils.join(StringUtils.addSeparator(captchaPath), img);
		File file = new File(path);
		try {
			if (file.exists())
				return ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String saveCaptcha(BufferedImage image, long time) {
		String fileName = StringUtils.join(time, ".jpg");
		String path = StringUtils.join(captchaPath, fileName);
		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(new File(path));
			ImageIO.write(image, "jpg", fileOut);
			return StringUtils.join(captchaUrlPath, fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
